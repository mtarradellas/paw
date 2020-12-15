import React from 'react';
import {Pagination, Rate, Table} from "antd";
import {useTranslation} from "react-i18next";

function Reviews({userId, reviewsPagination}){
    const {t} = useTranslation('userView');

    console.log(reviewsPagination.reviews);

    const reviewColumns = [
        {
            title: t('reviews.user'),
            dataIndex: 'username'
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

    return <>
            {
                reviewsPagination.reviews !== null && reviewsPagination.reviews.length > 0 &&
                <>
                    <h1><b>{t('reviewsTitle')}:</b></h1>

                    <Table
                        bordered={false}
                        columns={reviewColumns}
                        dataSource={reviewsPagination.reviews} size="small"
                        loading={reviewsPagination.fetching}
                        pagination={
                            <Pagination current={reviewsPagination.currentPage} total={reviewsPagination.amount}
                                        pageSize={reviewsPagination.pageSize} onChange={reviewsPagination.onPageChange}/>
                        }
                    />
                </>
            }
        </>
}

export default Reviews;