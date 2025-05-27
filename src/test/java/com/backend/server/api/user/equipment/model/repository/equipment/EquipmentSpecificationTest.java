package com.backend.server.api.user.equipment.model.repository.equipment;

import com.backend.server.api.user.equipment.dto.equipment.EquipmentListRequest;
import com.backend.server.config.AbstractPostgresContainerTest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EquipmentSpecificationTest extends AbstractPostgresContainerTest {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentCategoryRepository equipmentCategoryRepository;

    @Autowired
    private EquipmentModelRepository equipmentModelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private EquipmentCategory savedCategory;
    private EquipmentModel savedModel;
    private User savedUser;

    @BeforeEach
    void setUp() {
        // 테스트 데이터
        savedCategory = equipmentCategoryRepository.save(EquipmentCategory.builder()
                .name("카메라")
                .englishCode("CAM")
                .maxRentalCount(10)
                .build());

        savedModel = equipmentModelRepository.save(EquipmentModel.builder()
                .name("SONY-A7000")
                .englishCode("SON")
                .category(savedCategory)
                .build());

        savedUser = userRepository.save(User.builder()
                .name("홍길동")
                .birthDate(LocalDate.of(2000, 1, 1))
                .group("A반")
                .major("테스트전공")
                .email("asdf@asdf.sadf")
                .department("테스트학부")
                .nickname("테스트쟁이")
                .grade(4)
                .gender("남")
                .professor(null)
                .phoneNumber("010-2312-1234")
                .role(Role.ROLE_SUPER_ADMIN)
                .studentNumber("202300003")
                .build());

        equipmentRepository.save(Equipment.builder()
                .equipmentCategory(savedCategory)
                .equipmentModel(savedModel)
                .serialNumber("CAMSON0001")
                .status(Status.AVAILABLE)
                .renter(savedUser)
                .semesterSchedule(null)
                .restrictionGrade("3")
                .brokenCount(0L)
                .description("테스트")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .build());

        equipmentRepository.save(Equipment.builder()
                .equipmentCategory(savedCategory)
                .equipmentModel(savedModel)
                .serialNumber("CAMSON0002")
                .status(Status.AVAILABLE)
                .renter(savedUser)
                .semesterSchedule(null)
                .restrictionGrade("3")
                .brokenCount(0L)
                .description("테스트")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .build());

        equipmentRepository.save(Equipment.builder()
                .equipmentCategory(savedCategory)
                .equipmentModel(savedModel)
                .serialNumber("CAMSON0003")
                .status(Status.AVAILABLE)
                .renter(savedUser)
                .semesterSchedule(null)
                .restrictionGrade("4")
                .brokenCount(0L)
                .description("테스트")
                .managerId(1L)
                .rentalCount(0L)
                .repairCount(0L)
                .build());

        em.flush();
        em.clear();
        // 디버깅을 위한 로그
        System.out.println("Saved Category ID: " + savedCategory.getId());
        System.out.println("Saved Model ID: " + savedModel.getId());
        System.out.println("Saved User ID: " + savedUser.getId());
    }


    @Test
    void specification_shouldFilterRestrictGrade() {
        // given - 3학년인 사람
        Integer userGrade = 3;

        EquipmentListRequest request = EquipmentListRequest.builder()
                .categoryId(savedCategory.getId())
                .build();

        // when
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, userGrade);
        List<Equipment> result = equipmentRepository.findAll(spec);

        // 디버깅을 위한 로그
        System.out.println("=== 학년 제한 필터링 테스트 결과 ===");
        System.out.println("사용자 학년: " + userGrade);
        System.out.println("필터링된 장비 수: " + result.size());

        for (Equipment equipment : result) {
            System.out.println("장비 시리얼: " + equipment.getSerialNumber() +
                    ", 제한학년: " + equipment.getRestrictionGrade());
        }

        // then - 4학년 제한 장비만 조회되어야 함
        assertThat(result).hasSize(1); // 4학년 제한 장비 1개만
        assertThat(result.get(0).getSerialNumber()).isEqualTo("CAMSON0003");
        assertThat(result.get(0).getRestrictionGrade()).isEqualTo("4");

        // 3학년 제한 장비들은 결과에 포함되지 않아야 함
        boolean hasGrade3Equipment = result.stream()
                .anyMatch(eq -> "3".equals(eq.getRestrictionGrade()));
        assertThat(hasGrade3Equipment).isFalse();
    }

    @Test
    void specification_shouldShowAllEquipmentWhenUserGradeIsNull() {
        // given - userGrade가 null인 경우 (관리자 또는 학년 정보 없음)
        Integer userGrade = null;

        EquipmentListRequest request = EquipmentListRequest.builder()
                .categoryId(savedCategory.getId())
                .build();

        // when
        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, userGrade);
        List<Equipment> result = equipmentRepository.findAll(spec);

        // 디버깅 로그
        System.out.println("=== 학년 null 테스트 결과 ===");
        System.out.println("사용자 학년: null");
        System.out.println("조회된 장비 수: " + result.size());

        // then - 모든 장비가 조회되어야 함 (학년 제한 없음)
        assertThat(result).hasSize(3); // 모든 장비
    }

    @Test
    void filterEquipments_shouldReturnResultMatchingAllConditionsExceptUserGrade(){
        EquipmentListRequest request = EquipmentListRequest.builder()
                .categoryId(savedCategory.getId())
                .modelName("SONY")
                .status(Status.AVAILABLE.name())
                .renterName("홍길동")
                .searchKeyword("CAMSON0001")
                .build();

        Specification<Equipment> spec = EquipmentSpecification.filterEquipments(request, null);
        List<Equipment> result = equipmentRepository.findAll(spec);

        // then
        assertThat(result).hasSize(1);
        Equipment found = result.get(0);
        assertThat(found.getEquipmentCategory().getId()).isEqualTo(savedCategory.getId());
        assertThat(found.getEquipmentModel().getName()).contains("SONY");
        assertThat(found.getStatus()).isEqualTo(Status.AVAILABLE);
        assertThat(found.getRenter().getName()).isEqualTo("홍길동");
        assertThat(found.getSerialNumber()).isEqualTo("CAMSON0001");
    }
}
