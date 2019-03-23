import java.util.ArrayList;
import java.util.List;

class Helpers {

    /**
     * Converts the given value to bytes.
     * @param value String value, if unit not specified then
     *              will be treated as bytes.
     * @return int This returns the bytes after conversion.
     */
    int convertToBytes(String value){
        List<String> numberAndUnitValue = extractNumberAndUnit(value);
        String number = numberAndUnitValue.get(0);
        String unit = numberAndUnitValue.get(1);
        switch(unit.toUpperCase()) {
            case "KB":
                return Integer.parseInt(number) * 1024;
            case "MB":
                return Integer.parseInt(number) * 1048576;
            case "GB":
                return Integer.parseInt(number) * 1073741824;
            default:
                return Integer.parseInt(number);
        }

    }

    /**
     * Splits the given String into Unit and number.
     * @param str The only parameter, string value that
     *            needs to be split.
     * @return List of String values, index 0 will be number
     * and index 1 will be the unit.
     */
    private static List<String> extractNumberAndUnit(final String str) {

        if(str == null || str.isEmpty()) return null;
        List<String> numberAndUnit = new ArrayList<>();
        StringBuilder number = new StringBuilder();
        StringBuilder chars = new StringBuilder();
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                number.append(c);
            }
            else{
                chars.append(c);
            }
        }
        numberAndUnit.add(number.toString());
        numberAndUnit.add(chars.toString());
        return numberAndUnit;
    }
}
