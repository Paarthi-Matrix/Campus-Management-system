package com.ideas2it.cms.helper;

import com.ideas2it.cms.controller.AdminController;
import com.ideas2it.cms.customexception.PreCondtionValidationException;
import com.ideas2it.cms.dto.StudentDto;
import com.ideas2it.cms.util.BloodgroupUtil;
import com.ideas2it.cms.util.ConversionUtil;
import com.ideas2it.cms.util.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ObjectUtils;

public class RequestValidator {
    private static final Logger logger = LogManager.getLogger(AdminController.class);

    /**
     * <p>
     * Validates the input request for adding a student.
     * </p>
     * <p>
     * Note:
     * <ul>
     *     <li>Checks for valid blood group using BloodgroupUtil.</li>
     *     <li>Validates date of birth using DateUtil.</li>
     *     <li>Ensures the preferred grade is within the valid range.</li>
     *     <li>Returns appropriate ResponseEntity for invalid inputs or null if the input is valid.</li>
     * </ul>
     * </p>
     * @param studentDto The student request data transfer object containing student details.
     * @return ResponseEntity<String> The HTTP response entity containing validation errors or null if valid.
     */
    public static void insertStudentRequestValidator(StudentDto studentDto) {
        String errorMessage;
        try{
            String bloodGroup = BloodgroupUtil.validateBloodGroup(studentDto.getBloodGroup());
            if(ObjectUtils.isEmpty(bloodGroup)) {
                errorMessage = studentDto.getBloodGroup() + " is a invalid blood group!";
                throw new PreCondtionValidationException(errorMessage);
            }
        } catch(IllegalArgumentException e) {
            throw new PreCondtionValidationException(e.getMessage(), e);
        }
        DateValidationResult validationResult = DateUtil.checkValidDateAndAge(studentDto.getDateOfBirth(), "dd/MM/yyyy");
        switch (validationResult) {
            case VALID_DATE:
                 break;
            case INVALID_DATE:
                logger.warn("User entered invalid date format: {}", studentDto.getDateOfBirth());
                errorMessage= studentDto.getDateOfBirth() + " is not a valid date format";
                throw new PreCondtionValidationException(errorMessage);
            case FUTURE_DATE:
                logger.warn("User entered a future date: {}", studentDto.getDateOfBirth());
                errorMessage = studentDto.getDateOfBirth() + " is a future date and not valid ";
                throw new PreCondtionValidationException(errorMessage);
            case OVER_18:
                logger.warn("{} User's age is is not under constrains. Must be below 18! ", studentDto.getDateOfBirth());
                errorMessage = studentDto.getDateOfBirth() + " User's age is is not under constrains. Must be below 18!" ;
                throw new PreCondtionValidationException(errorMessage);
            case UNDER_3:
                logger.warn("{} User's age is is not under constrains. Must be at least 3 years old! ", studentDto.getDateOfBirth());
                errorMessage = studentDto.getDateOfBirth() + " User's age is is not under constrains. Must be at least 3 years old!";
                throw new PreCondtionValidationException(errorMessage);
        }

        if (ObjectUtils.isEmpty(studentDto.getGradePreferred())) {
            logger.warn("The preferred grade is null ");
            errorMessage = "The preferred grade should not be null";
            throw new PreCondtionValidationException(errorMessage);
        }
        if (ConversionUtil.stringToInt(studentDto.getGradePreferred()) < 1
                || ConversionUtil.stringToInt(studentDto.getGradePreferred()) > 12) {
            logger.warn("The preferred grade must be in range of 1 to 12 only ");
             errorMessage = studentDto.getGradePreferred() +
                   " The preferred grade must be in range of 1 to 12 only ";
             throw new PreCondtionValidationException(errorMessage);
        }
    }
}
