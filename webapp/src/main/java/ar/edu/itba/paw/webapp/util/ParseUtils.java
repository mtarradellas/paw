package ar.edu.itba.paw.webapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ParseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParseUtils.class);

    public static int parsePage(String page) {
        int pageNum = 1;
        if(page != null) {
            try {
                pageNum = Integer.parseInt(page);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid page ({}) parameter", page);
            }
        }
        return pageNum;
    }

    public static <E extends Enum<E>> E parseStatus(Class<E> enumClass, String statusStr) {
        E status = null;
        E[] values = enumClass.getEnumConstants();
        if(statusStr != null) {
            try {
                int idx = Integer.parseInt(statusStr);
                status = values[idx];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
                LOGGER.debug("Invalid status ({}) parameter", statusStr);
            }
        }
        return status;
    }

    public static Long parseSpecies(String speciesStr) {
        Long species = null;
        if (speciesStr != null && !speciesStr.equalsIgnoreCase("any")) {
            try {
                species = Long.parseLong(speciesStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid species ({}) parameter", speciesStr);
            }
        }
        return species;
    }

    public static Long parseBreed(String breedStr) {
        Long breed = null;
        if (breedStr != null && !breedStr.equalsIgnoreCase("any")) {
            try {
                breed = Long.parseLong(breedStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid breed ({}) parameter", breedStr);
            }
        }
        return breed;
    }

    public static Long parseProvince(String provinceStr) {
        Long province = null;
        if (provinceStr != null && !provinceStr.equalsIgnoreCase("any")) {
            try {
                province = Long.parseLong(provinceStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid province ({}) parameter", provinceStr);
            }
        }
        return province;
    }

    public static Long parseDepartment(String departmentStr) {
        Long department = null;
        if (departmentStr != null && !departmentStr.equalsIgnoreCase("any")) {
            try {
                department = Long.parseLong(departmentStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid department ({}) parameter", departmentStr);
            }
        }
        return department;
    }

    public static boolean isAllowedFind(String find) {
        return find == null || find.matches("^[a-zA-Z0-9 \u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                "\u00F3\u00FA\u00F1\u00FC]*$");
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
        return null;
    }

    public static String parseCriteria(String criteria) {
        if (criteria == null || criteria.equalsIgnoreCase("any")) return null;
        return criteria;
    }

    public static String parseOrder(String order) {
        if (order != null && order.equalsIgnoreCase("desc")) return "desc";
        return "asc";
    }

    public static int[] parseRange(String range) {
        if (range == null) return new int[]{0, -1};
        int[] price;
        switch (range) {
            case "0" : price = new int[]{0, 0}; break;
            case "1" : price = new int[]{1, 4999}; break;
            case "2" : price = new int[]{5000, 9999}; break;
            case "3" : price = new int[]{10000, 14999}; break;
            case "4" : price = new int[]{15000, 19999}; break;
            case "5" : price = new int[]{20000, 24999}; break;
            case "6" : price = new int[]{25000, -1}; break;
            default: price = new int[]{0, -1}; break;
        }
        return price;
    }

    public static int[] parsePrice(String minPrice, String maxPrice) {
        int minPriceNum = parseMinPrice(minPrice);
        int maxPriceNum = parseMaxPrice(maxPrice);

        if (maxPriceNum != -1 && minPriceNum > maxPriceNum) return new int[]{0, -1};
        return new int[]{minPriceNum, maxPriceNum};
    }

    public static int parseMinPrice(String price) {
        if (price == null) return 0;
        int priceNum = 0;
        try {
            priceNum = Integer.parseInt(price);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid min price ({}) parameter", price);
        }
        if (priceNum < 0) priceNum = 0;
        return priceNum;
    }

    public static int parseMaxPrice(String price) {
        if (price == null) return -1;
        int priceNum = -1;
        try {
            priceNum = Integer.parseInt(price);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid max price ({}) parameter", price);
        }
        if (priceNum < -1) priceNum = -1;
        return priceNum;
    }

    public static int parseReviewScore(String scoreStr) {
        if (scoreStr == null) return -1;
        int score = -1;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid score ({}) parameter", scoreStr);
        }
        if (score < 0 || score > 5) score = -1;
        return score;
    }

    public static Long parseUser(String userStr) {
        Long userId = null;
        if (userStr != null && !userStr.equalsIgnoreCase("any")) {
            try {
                userId = Long.parseLong(userStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid user id ({}) parameter", userStr);
            }
        }
        return userId;
    }

    public static Long parsePet(String petStr) {
        Long petId = null;
        if (petStr != null && !petStr.equalsIgnoreCase("any")) {
            try {
                petId = Long.parseLong(petStr);
            } catch (NumberFormatException ex) {
                LOGGER.debug("Invalid pet id ({}) parameter", petStr);
            }
        }
        return petId;
    }

    public static int parseScore(String scoreStr, int defaultScore) {
        if (scoreStr == null) return defaultScore;
        int score = defaultScore;
        try {
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException ex) {
            LOGGER.debug("Invalid score ({}) parameter", scoreStr);
        }
        if (score < 1 || score > 5) score = defaultScore;
        return score;
    }

}
