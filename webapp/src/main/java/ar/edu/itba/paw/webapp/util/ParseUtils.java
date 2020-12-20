package ar.edu.itba.paw.webapp.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ar.edu.itba.paw.webapp.dto.PetDto;
import ar.edu.itba.paw.webapp.dto.ReviewDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.exception.BadRequestException;

public class ParseUtils {

    public static int parsePage(int page) {
        if (page < 1) {
            String hint = "Page number must be greater than 0";
            throw new BadRequestException("page", String.valueOf(page), hint);
        }
        return page;
    }

    public static <E extends Enum<E>> E parseStatus(Class<E> enumClass, int statusIdx) {
        if (statusIdx < 0) return null;
        E[] values = enumClass.getEnumConstants();
        E status;
        try {
            status = values[statusIdx];
        } catch (ArrayIndexOutOfBoundsException ex) {
            String hint = "For any valid status, use -1";
            throw new BadRequestException("status", String.valueOf(statusIdx), hint);
        }
        return status;
    }

    public static Long parseSpecies(long speciesId) {
        if (speciesId == 0) return null;
        if (speciesId < 1) {
            String hint = "Species ID must be greater than 0, or 0 for any species";
            throw new BadRequestException("species ID", String.valueOf(speciesId), hint);
        }
        return speciesId;
    }

    public static Long parseBreed(long breedId) {
        if (breedId == 0) return null;
        if (breedId < 1) {
            String hint = "Breed ID must be greater than 0, or 0 for any breed";
            throw new BadRequestException("Breed ID", String.valueOf(breedId), hint);
        }
        return breedId;
    }

    public static Long parseProvince(long provinceId) {
        if (provinceId == 0) return null;
        if (provinceId < 1) {
            String hint = "Province ID must be greater than 0, or 0 for any province";
            throw new BadRequestException("province ID", String.valueOf(provinceId), hint);
        }
        return provinceId;
    }

    public static Long parseDepartment(long departmentId) {
        if (departmentId == 0) return null;
        if (departmentId < 1) {
            String hint = "Department ID must be greater than 0, or 0 for any department";
            throw new BadRequestException("department ID", String.valueOf(departmentId), hint);
        }
        return departmentId;
    }

    public static boolean isAllowedFind(String find) {
        if (find != null && !find.matches("^[a-zA-Z0-9 \u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                "\u00F3\u00FA\u00F1\u00FC]*$")) {
            String hint = "There are some invalid characters in the find text";
            throw new BadRequestException("find", find, hint);
        }
        return true;
    }

    public static List<String> parseFind(String find) {
        if(find == null) return null;
        String[] splitStr = find.trim().split("\\s+");
        return Arrays.asList(splitStr);
    }

    public static String parseGender(String gender) {
        if (gender == null) return null;
        if (gender.equalsIgnoreCase("male")) return "male";
        if (gender.equalsIgnoreCase("female")) return "female";
        String hint = "Gender must be 'male' or 'female', or null for both";
        throw new BadRequestException("gender", gender, hint);
    }

    public static String parseCriteria(String criteria) {
        if (criteria == null || criteria.equalsIgnoreCase("any")) return null;
        return criteria;
    }

    public static String parseOrder(String order) {
        if (order != null && order.equalsIgnoreCase("desc")) return "desc";
        return "asc";
    }

    public static void parseReviewScore(Integer score) {
        if (score == null || score < 1 || score > 5) {
            String hint = "Score must be between 1 and 5 inclusive";
            throw new BadRequestException("score", String.valueOf(score), hint);
        }
    }

    public static void parseReviewScore(int minScore, int maxScore) {
        parseReviewScore(minScore);
        parseReviewScore(maxScore);
        if (minScore > maxScore) {
            String hint = "Max score must be greater or equal than min score";
            throw new BadRequestException("score", "[" + minScore + ", " + maxScore + "]", hint);
        }
    }

    public static void parseReviewDescription(String description) {
        if (description != null && description.length() > 2048) {
            String hint = "Description must contain less than 2048 characters";
            throw new BadRequestException("description", description, hint);
        }
    }

    public static Long parseUserId(long userId) {
        if (userId == 0) return null;
        if (userId < 1) {
            String hint = "User ID must be grater than 0, or 0 for any user";
            throw new BadRequestException("user ID", String.valueOf(userId), hint);
        }
        return userId;
    }

    public static void parseUsername(String  username) {
        if (username == null || username.length() > 255 || username.length() < 4 ||
                !username.matches("^[a-zA-Z0-9\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                        "\u00F3\u00FA\u00F1\u00FC]*$")) {
            String hint = "Username must be between 4 and 255 valid characters";
            throw new BadRequestException("username", username, hint);
        }
    }

    public static void parseMail(String  mail) {
        if (mail == null || mail.length() > 254 || mail.length() < 3 || !mail.matches("^.+@.+$")) {
            String hint = "Mail must be between 3 and 254 characters, including @";
            throw new BadRequestException("mail", mail, hint);
        }
    }

    public static void parsePassword(String  password) {
        if (password == null || password.length() > 255 || password.length() < 4) {
            String hint = "Password must be between 4 and 255 characters";
            throw new BadRequestException("password", "***", hint);
        }
    }

    public static Long parsePetId(long petId) {
        if (petId == 0) return null;
        if (petId < 1) {
            String hint = "Pet ID must be grater than 0, or 0 for any pet";
            throw new BadRequestException("Pet ID", String.valueOf(petId), hint);
        }
        return petId;
    }

    public static Long parseQuestionId(long questionId) {
        if (questionId == 0) return null;
        if (questionId < 1) {
            String hint = "Question ID must be grater than 0, or 0 for any user";
            throw new BadRequestException("user ID", String.valueOf(questionId), hint);
        }
        return questionId;
    }

    public static String parseQuestion(String question) {
        if (question == null || question.length() > 250 || question.length() < 1) {
            String hint = "Question must have between 1 and 250 characters";
            throw new BadRequestException("Question", question, hint);
        }
        return question;
    }

    public static void parseReview(ReviewDto review) {
        if (review == null || review.getTargetId() == null || review.getScore() == null) {
            String hint = "Review is missing a required attribute";
            throw new BadRequestException("review", "user, target or score", hint);
        }
        parseUserId(review.getTargetId());
        parseReviewScore(review.getScore());
        parseReviewDescription(review.getDescription());
    }

    public static void parseUser(UserDto user) {
        if (user == null || user.getMail() == null || user.getPassword() == null || user.getUsername() == null) {
            String hint = "User is missing a required attribute";
            throw new BadRequestException("user", "username, password or mail", hint);
        }

        parseUsername(user.getUsername());
        parseMail(user.getMail());
        parsePassword(user.getPassword());
    }

    public static void parsePet(PetDto pet) {
        if (pet == null || pet.getPetName() == null) {
            throw new BadRequestException("Invalid or missing required fields");
        }
        pet.setPetName(pet.getPetName().trim().replaceAll(" +", " "));
        if (pet.getPetName().length() == 0 || pet.getPetName().length() > 255) {
            throw new BadRequestException("Invalid or missing required fields");
        }
        if (pet.getGender() == null || pet.getPrice() == null || pet.getSpeciesId() == null || pet.getBreedId() == null || 
            pet.getProvinceId() == null || pet.getDepartmentId() == null) {
                throw new BadRequestException("Invalid or missing required fields");
        }
    }

    public static void parsePet(String petName, String gender, Long speciesId, Long breedId, Long provinceId, Long departmentId) {
        petName = (petName.trim().replaceAll(" +", " "));
        if (petName.length() == 0 || petName.length() > 255) {
            throw new BadRequestException("Invalid or missing required fields");
        }
        if (gender == null || speciesId == null || breedId == null || provinceId == null || departmentId == null) {
            throw new BadRequestException("Invalid or missing required fields");
        }
    }
    public static List<Long> parseImagesToDelete(String toDelete) {
        if(toDelete == null || toDelete.length() == 0) return null;
        List<Long> imagesToDelete = new ArrayList<>();
        String[] res = toDelete.split("[,]", 0);
        try {
            for (String str : res) {
                imagesToDelete.add(Long.parseLong(str));
            }
        } catch (Exception ex) {
            throw new BadRequestException("Format of images to delete is invalid");
        }
        return imagesToDelete;
    }

    public static LocalDateTime parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        return LocalDateTime.parse(date, formatter);
    }
}