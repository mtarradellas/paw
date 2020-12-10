import React from 'react';
import {useTranslation} from "react-i18next";
import _ from 'lodash';

function useTranslatedErrors(namespace, prefix=''){
    const {t} = useTranslation(namespace);

    function translateErrors(errors){
        return _.mapValues(errors, ({message, params}) => (t(prefix + message)));
    }

    return translateErrors;
}

export default useTranslatedErrors;