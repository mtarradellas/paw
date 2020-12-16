import React from 'react';
import {Divider, Pagination, Rate, Table} from "antd";
import {useTranslation} from "react-i18next";
import {USER} from "../../constants/routes";
import {Link} from "react-router-dom";

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

    return <>
            {
                reviewsPagination.reviews !== null && reviewsPagination.reviews.length > 0 &&
                <>
                    <h1><b>{t('reviewsTitle')}:</b></h1>

                    <Divider orientation={"left"}>
                        <Pagination current={reviewsPagination.currentPage} total={reviewsPagination.amount}
                                    pageSize={reviewsPagination.pageSize} onChange={reviewsPagination.onPageChange}/>
                    </Divider>

                    <Table
                        bordered={false}
                        columns={reviewColumns}
                        dataSource={reviewsPagination.reviews} size="small"
                        loading={reviewsPagination.fetching}
                        pagination={false}
                    />

                    <Divider orientation={"left"}>
                        <Pagination current={reviewsPagination.currentPage} total={reviewsPagination.amount}
                                    pageSize={reviewsPagination.pageSize} onChange={reviewsPagination.onPageChange}/>
                    </Divider>

                </>
            }
        </>
}

export default Reviews;