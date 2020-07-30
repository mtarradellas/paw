package ar.edu.itba.paw.webapp.util;

import ar.edu.itba.paw.webapp.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ParseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtils.class);

    public static int parsePage(int page) {
        if (page < 1) {
            String hint = "Page number must be greater than 0.";
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
            String hint = "For any valid status, use -1.";
            throw new BadRequestException("status", String.valueOf(statusIdx), hint);
        }
        return status;
    }

    public static Long parseSpecies(long speciesId) {
        if (speciesId == 0) return null;
        if (speciesId < 1) {
            String hint = "Species ID must be greater than 0, or 0 for any species.";
            throw new BadRequestException("species ID", String.valueOf(speciesId), hint);
        }
        return speciesId;
    }

    public static Long parseBreed(long breedId) {
        if (breedId == 0) return null;
        if (breedId < 1) {
            String hint = "Breed ID must be greater than 0, or 0 for any breed.";
            throw new BadRequestException("Breed ID", String.valueOf(breedId), hint);
        }
        return breedId;
    }

    public static Long parseProvince(long provinceId) {
        if (provinceId == 0) return null;
        if (provinceId < 1) {
            String hint = "Province ID must be greater than 0, or 0 for any province.";
            throw new BadRequestException("province ID", String.valueOf(provinceId), hint);
        }
        return provinceId;
    }

    public static Long parseDepartment(long departmentId) {
        if (departmentId == 0) return null;
        if (departmentId < 1) {
            String hint = "Department ID must be greater than 0, or 0 for any department.";
            throw new BadRequestException("department ID", String.valueOf(departmentId), hint);
        }
        return departmentId;
    }

    public static boolean isAllowedFind(String find) {
        if (find != null && !find.matches("^[a-zA-Z0-9 \u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                "\u00F3\u00FA\u00F1\u00FC]*$")) {
            String hint = "There are some invalid characters in the find text.";
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
        String hint = "Gender must be 'male' or 'female', or null for both.";
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

    public static int[] parseRange(int range) {
        int[] price;
        switch (range) {
            case 0 : price = new int[]{0, -1}; break; // Any price
            case 1 : price = new int[]{0, 0}; break;
            case 2 : price = new int[]{1, 4999}; break;
            case 3 : price = new int[]{5000, 9999}; break;
            case 4 : price = new int[]{10000, 14999}; break;
            case 5 : price = new int[]{15000, 19999}; break;
            case 6 : price = new int[]{20000, 24999}; break;
            case 7 : price = new int[]{25000, -1}; break;
            default  : String hint = "Range must be between 1 and 7 inclusive, or 0 for any range.";
                       throw new BadRequestException("price range", String.valueOf(range), hint);
        }
        return price;
    }

    public static int parseReviewScore(int score) {
        if (score < 0 || score > 5) {
            String hint = "Score must be between 0 and 5 inclusive.";
            throw new BadRequestException("score", String.valueOf(score), hint);
        }
        return score;
    }

    public static Long parseUser(long userId) {
        if (userId == 0) return null;
        if (userId < 1) {
            String hint = "User ID must be grater than 0, or 0 for any user.";
            throw new BadRequestException("user ID", String.valueOf(userId), hint);
        }
        return userId;
    }

    public static Long parsePet(long petId) {
        if (petId == 0) return null;
        if (petId < 1) {
            String hint = "Pet ID must be grater than 0, or 0 for any pet.";
            throw new BadRequestException("Pet ID", String.valueOf(petId), hint);
        }
        return petId;
    }
}
