package com.backend.server.api.admin.equipment.model.repository;

import com.backend.server.model.entity.User;
import com.backend.server.model.entity.enums.Gender;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.enums.Status;
import com.backend.server.model.entity.equipment.Equipment;
import com.backend.server.model.entity.equipment.EquipmentCategory;
import com.backend.server.model.entity.equipment.EquipmentModel;
import com.backend.server.model.repository.UserRepository;
import com.backend.server.model.repository.equipment.EquipmentCategoryRepository;
import com.backend.server.model.repository.equipment.EquipmentModelRepository;
import com.backend.server.model.repository.equipment.EquipmentRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AdminEquipmentSpecificationTest {

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
                .gender(Gender.M)
                .professor(null)
                .phoneNumber("01023121234")
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


}
