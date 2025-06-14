package com.backend.server.api.admin.user.service;

import com.backend.server.model.entity.Professor;
import com.backend.server.model.entity.enums.Gender;
import com.backend.server.model.entity.enums.Role;
import com.backend.server.model.entity.User;
import com.backend.server.model.repository.user.ProfessorRepository;
import com.backend.server.model.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 엑셀 파일을 처리하는 서비스 클래스
 * 이 클래스는 업로드된 엑셀 파일로부터 사용자 정보를 읽어 데이터베이스에 저장하는 기능을 제공합니다.
 */
@Slf4j  // 로깅 기능을 위한 어노테이션 (로그 기록용)
@Service  // 스프링 서비스 컴포넌트로 지정
public class AdminImportExcelService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * 엑셀 파일에서 사용자 정보를 읽어 데이터베이스에 일괄 등록합니다.
     * 첫 번째 행의 헤더를 기반으로 필요한 필드를 동적으로 찾아 매핑합니다.
     *
     * @param file 업로드된 엑셀 파일
     * @return 처리된 사용자 수
     * @throws IOException 파일 처리 중 오류가 발생한 경우
     */



    public int importUsersFromExcel(MultipartFile file) throws IOException {
        //파일 확장자 뭔지 확인하는부분
        String fileExtension = getFileExtension(file.getOriginalFilename());
        if (!fileExtension.equals("xlsx") && !fileExtension.equals("xls")) {
            throw new IllegalArgumentException("엑셀 파일(.xlsx 또는 .xls)만 업로드 가능합니다.");
        }

        int processedCount = 0;
        
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getPhysicalNumberOfRows() <= 1) {
                throw new IllegalArgumentException("데이터가 없는 엑셀 파일입니다.");
            }
            
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("헤더 행이 없습니다.");
            }
            
            // 헤더 행에서 각 컬럼의 인덱스를 찾아 매핑
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null) {
                    headerMap.put(getCellValue(cell), i);
                }
            }
            
            log.info("헤더 매핑: {}", headerMap);
            
            // 필요한 필수 컬럼이 있는지 확인
            validateRequiredHeaders(headerMap);
            
            // 헤더를 기반으로 컬럼 인덱스 찾기
            Integer nameIndex = headerMap.get("성명");
            Integer studentNumberIndex = headerMap.get("학번");
            Integer gradeIndex = headerMap.get("학년");
            Integer genderIndex = headerMap.get("성별");
            //지도교수 여기
            Integer professorIndex = headerMap.get("지도교수");
            Integer phoneIndex = headerMap.get("휴대폰번호");
            Integer emailIndex = headerMap.get("이메일");
            Integer birthDateIndex = headerMap.get("생년월일");
            Integer classIndex = headerMap.get("반");
            
            // 데이터 행 처리 (첫 번째 행은 헤더이므로 건너뜀)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isEmptyRow(row)) continue;
                
                String studentNumber = "";
                String name = "";
                
                try {  //이게 문제인가? 근데아닌듯
                    // studentNumber = studentNumberIndex != null 
                    //     ? getCellValue(row.getCell(studentNumberIndex)) 
                    //     : "";
                    
                    studentNumber = getCellValue(row.getCell(studentNumberIndex));
                    
                    // 사용자 생성
                    User.UserBuilder userBuilder = User.builder();
                    
                    
                    // 필드 매핑
                    name = nameIndex != null ? getCellValue(row.getCell(nameIndex)) : "";

                    
                    userBuilder.name(name);
                    userBuilder.studentNumber(studentNumber);
                    userBuilder.nickname(name); // 닉네임은 이름으로 설정
                    
                    if (gradeIndex != null) userBuilder.grade(Integer.parseInt(getCellValue(row.getCell(gradeIndex))));
                    if (genderIndex != null) userBuilder.gender(Gender.fromKorean(getCellValue(row.getCell(genderIndex))));

                    //교수찾기
                    if (professorIndex != null) {
                        String profName = getCellValue(row.getCell(professorIndex)).trim();
                        if (!profName.isEmpty()) {
                            // 교수찾기
                            Professor professor = professorRepository
                                    .findByName(profName)
                                    // 없으면 만들기
                                    .orElseGet(() -> {
                                        Professor newProf = Professor.builder()
                                                .name(profName)
                                                .description("자동 생성된 교수님입니다.")
                                                .build();
                                        return professorRepository.save(newProf);
                                    });
                            // 유저에 교수할당
                            userBuilder.professor(professor);
                        }
                    }
                    if (phoneIndex != null) userBuilder.phoneNumber(getCellValue(row.getCell(phoneIndex)));
                    if (emailIndex != null) userBuilder.email(getCellValue(row.getCell(emailIndex)));
                    if (classIndex != null) userBuilder.group(getCellValue(row.getCell(classIndex)));
                    
                    // 생년월일 처리
                    if (birthDateIndex != null) {
                        String birthDateStr = getCellValue(row.getCell(birthDateIndex));
                        try {
                            userBuilder.birthDate(LocalDate.parse(birthDateStr));  // yyyy-MM-dd 형식 예상
                        } catch (Exception e) {
                            log.error("생년월일 형식에 문제가 있습니다: {}", birthDateStr);
                            throw new IllegalArgumentException("생년월일 형식이 올바르지 않습니다: " + birthDateStr);
                        }
                    } else {
                        log.error("생년월일 컬럼이 없습니다.");
                        throw new IllegalArgumentException("생년월일 컬럼이 필요합니다.");
                    }
                    
                    // 필수 필드와 기본값 설정
                    userBuilder.password(passwordEncoder.encode(studentNumber)); //기본 비밀번호는 학번임.
                    userBuilder.restrictionCount(0);
                    userBuilder.reportCount(0);
                    userBuilder.role(Role.valueOf("ROLE_USER"));
                    
                    User user = userBuilder.build();
                    
                    // 각 사용자를 개별적으로 저장
                    userRepository.save(user);
                    processedCount++;
                    
                } catch (Exception e) {
                    log.error("행 {}의 데이터 처리 중 오류 (학번: {}, 이름: {}): {}", i + 1, studentNumber, name, e.getMessage());
                }
            }
            
            log.info("{}명의 사용자 정보를 성공적으로 등록했습니다.", processedCount);
            return processedCount;
            
        } catch (Exception e) {
            log.error("엑셀 파일 처리 중 오류 발생: {}", e.getMessage());
            throw new IOException("엑셀 파일 처리 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
    /**
     * 파일 확장자를 반환합니다.
     */
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf('.');
        return (lastDotIndex == -1) ? "" : filename.substring(lastDotIndex + 1).toLowerCase();
    }
    /**
     * 셀 값을 문자열로 반환합니다.
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                // 숫자는 정수로 변환 (소수점 제거)
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((int) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getNumericCellValue());
                } catch (Exception e) {
                    try {
                        return cell.getStringCellValue();
                    } catch (Exception ex) {
                        return "";
                    }
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    /**
     * 필수 헤더가 엑셀 파일에 있는지 확인합니다.
     */
    private void validateRequiredHeaders(Map<String, Integer> headerMap) {
        List<String> requiredHeaders = List.of("성명", "학번");
        List<String> missingHeaders = new ArrayList<>();
        
        for (String header : requiredHeaders) {
            if (!headerMap.containsKey(header)) {
                missingHeaders.add(header);
            }
        }
        
        if (!missingHeaders.isEmpty()) {
            throw new IllegalArgumentException(
                "필수 헤더가 없습니다: " + String.join(", ", missingHeaders));
        }
    }
    
    /**
     * 행이 비어있는지 확인합니다.
     */
    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !getCellValue(cell).isEmpty()) {
                return false;
            }
        }
        return true;
    }
} 