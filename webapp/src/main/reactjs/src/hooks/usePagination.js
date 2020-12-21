import React, {useReducer} from 'react';
import _ from 'lodash';

const SAVE_ELEMENTS = "SAVE_ELEMENTS";
function savePageAction(start, end, elements){
    return {
        type: SAVE_ELEMENTS,
        start,
        end,
        elements
    }
}

const CHANGE_PAGE = "CHANGE_PAGE";
function changePageAction(index){
    return {
        type: CHANGE_PAGE,
        index
    }
}

const initialState = {
    index: 0,
    elements: [],
    pageSize: null,
    fetchPageSize: null,
    totalElements: null
};

const pagesReducer = (state, action) => {
    switch (action.type) {
        case SAVE_ELEMENTS:
        {
            const index = searchForIntersection(action, state.elements);
            break;
        }
    }
};

function addIndex(elems, start){
    return _.zip(
        _.times(elems.length, i => (i + start)),
        elems
    );
}

function mergeRanges(a, b){
    const {start: aStart, elements: aElements} = a;
    const {start: bStart, elements: bElements} = b;

    return _.assign(
        {},
        [addIndex(aElements, aStart), addIndex(bElements, bStart)]
    ).reduce((acc, val) => (_.concat([acc, Object.values(val)])), []);
}

function searchForIntersection(range, elements){
    const {start: rangeStart, end: rangeEnd} = range;

    return _.findIndex(elements, elem => {
        const {start, end} = elem;

        return (start <= rangeEnd && end >= rangeEnd) ||
            (rangeStart <= end && rangeEnd >= end) ||
            (start >= rangeStart && end <= rangeEnd) ||
            (rangeStart >= start && rangeEnd <= end);

    })
}

const usePagination = ({fetchPage, pageSize, fetchPageSize}) => {
    const [state, dispatch] = useReducer(pagesReducer, initialState);
    const {index, pages} = state;
    const currentPage = pages[index];
    const isFetching = _.isNil(currentPage);

    const loadPage = async (page) => {
        const startPage = _.floor(page / fetchPageSize);
        const endPage = _.ceil(pageSize / fetchPageSize + startPage);

        const pages = (await Promise.all(_.times(endPage - startPage, i => (fetchPage(i)))))
                     .reduce((acc, val) => (_.concat([acc, val])));

        dispatch(savePageAction(
            startPage,
            endPage,
            pages
        ))
    };

    const changePage = async (index) => {
        dispatch(changePageAction(index));

        await loadPage(index);
    };

    return {currentPage, isFetching, index, changePage};

};

export default usePagination;