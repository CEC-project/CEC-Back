package com.backend.server.api.admin.equipment.model.repository;


import com.backend.server.api.admin.equipment.dto.equipment.request.AdminEquipmentListRequest;
import com.backend.server.config.AbstractPostgresContainerTest;
import com.backend.server.model.entity.Equipment;
import com.backend.server.model.entity.EquipmentCategory;
import com.backend.server.model.entity.EquipmentModel;
import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import com.backend.server.model.repository.equipment.EquipmentSpecification;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AdminEquipmentSpecificationTest extends AbstractPostgresContainerTest {

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

        em.flush();
        em.clear();

        // 디버깅을 위한 로그
        System.out.println("Saved Category ID: " + savedCategory.getId());
        System.out.println("Saved Model ID: " + savedModel.getId());
        System.out.println("Saved User ID: " + savedUser.getId());
    }

    @Test
    void adminFilterEquipments_shouldReturnFilteredByModelNameAndStatusAndCategoryIdAndSerialNumberAndSearchKeywordAndRenterName() {
        // 실제 저장된 ID를 사용
        AdminEquipmentListRequest request = AdminEquipmentListRequest.builder()
                .modelName("SONY")
                .status(Status.AVAILABLE.name())
                .categoryId(savedCategory.getId())
                .serialNumber("CAMSON0001")
                .searchKeyword("sony")
                .renterName("홍길")
                .page(0)
                .size(10)
                .build();

        Specification<Equipment> spec = EquipmentSpecification.adminFilterEquipments(request);

        // when
        List<Equipment> result = equipmentRepository.findAll(spec);

        // 디버깅을 위한 로그
        System.out.println("Result size: " + result.size());
        if (!result.isEmpty()) {
            Equipment equipment = result.get(0);
            System.out.println("Model: " + equipment.getEquipmentModel().getName() +
                    ", Status: " + equipment.getStatus() +
                    ", Category ID: " + equipment.getEquipmentCategory().getId() +
                    ", Serial: " + equipment.getSerialNumber());
        }

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEquipmentModel().getName()).contains("SONY");
        assertThat(result.get(0).getStatus().name()).isEqualTo("AVAILABLE");
        assertThat(result.get(0).getEquipmentCategory().getId()).isEqualTo(savedCategory.getId());
        assertThat(result.get(0).getSerialNumber()).isEqualTo("CAMSON0001");
    }

    @Test
    void adminFilterEquipments_shouldFilterByRenterNameAndKeyword() {
        // given
        AdminEquipmentListRequest request = AdminEquipmentListRequest.builder()
                .renterName("홍길동")
                .searchKeyword("0001")
                .build();

        Specification<Equipment> spec = EquipmentSpecification.adminFilterEquipments(request);

        // when
        List<Equipment> result = equipmentRepository.findAll(spec);

        // 디버깅 로그
        System.out.println("Filter by renter name - Result size: " + result.size());

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRenter().getName()).isEqualTo("홍길동");
    }

    @Test
    void debugTest_checkAllEquipments() {
        // 모든 장비 조회해서 실제 데이터 확인
        List<Equipment> allEquipments = equipmentRepository.findAll();
        System.out.println("Total equipments: " + allEquipments.size());

        for (Equipment eq : allEquipments) {
            System.out.println("Equipment - ID: " + eq.getId() +
                    ", Model: " + eq.getEquipmentModel().getName() +
                    ", Serial: " + eq.getSerialNumber() +
                    ", Status: " + eq.getStatus() +
                    ", Category ID: " + eq.getEquipmentCategory().getId() +
                    ", Renter: " + (eq.getRenter() != null ? eq.getRenter().getName() : "null"));
        }
    }
}