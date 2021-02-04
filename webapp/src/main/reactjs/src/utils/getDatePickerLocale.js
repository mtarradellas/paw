import en_US from 'antd/es/date-picker/locale/en_US';
import es_ES from 'antd/es/date-picker/locale/es_ES';
import i18next from "i18next";

export default function getDatePickerLocale(){
    switch (i18next.language){
        case 'es':
            return es_ES;
        default:
            return en_US;
    }
}