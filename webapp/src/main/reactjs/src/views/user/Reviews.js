import React from 'react';
import {Divider, Pagination, Rate, Table} from "antd";
import {useTranslation} from "react-i18next";
import {USER} from "../../constants/routes";
import {Link} from "react-router-dom";
import _ from 'lodash';

function Reviews({userId, reviewsPagination}){
    const {t} = useTranslation('userView');

    const reviewColumns = [
        {
            title: t('reviews.user'),
            dataIndex: 'username',
            render: (text, {userId}) => (
                <Link to={USER + userId}>{text}</Link>
            )
        },
        {
            title: t('reviews.rating'),
            dataIndex: 'score',
            render: rating => (
                <Rate allowHalf disabled defaultValue={rating}/>
            )
        },
        {
            title: t('reviews.description'),
            dataIndex: 'description'
        }
    ];

    const shouldShowPagination = !_.isNil(reviewsPagination.amount) && !_.isNil(reviewsPagination.pageSize) &&
        reviewsPagination.amount > reviewsPagination.pageSize;

    return <>
            {
                reviewsPagination.reviews !== null && reviewsPagination.reviews.length > 0 &&
                <>
                    <h1><b>{t('reviewsTitle')}:</b></h1>

                    <Divider orientation={"left"}>
                        {
                            shouldShowPagination &&
                                <Pagination current={reviewsPagination.currentPage} total={reviewsPagination.amount}
                                        pageSize={reviewsPagination.pageSize} onChange={reviewsPagination.changePage}/>
                        }
                    </Divider>

                    <Table
                        bordered={false}
                        columns={reviewColumns}
                        dataSource={reviewsPagination.reviews} size="small"
                        loading={reviewsPagination.fetching}
                        pagination={false}
                    />

                    <Divider orientation={"left"}>
                        {
                            shouldShowPagination &&
                                <Pagination current={reviewsPagination.currentPage} total={reviewsPagination.amount}
                                        pageSize={reviewsPagination.pageSize} onChange={reviewsPagination.changePage}/>
                        }
                    </Divider>

                </>
            }
        </>
}

export default Reviews;