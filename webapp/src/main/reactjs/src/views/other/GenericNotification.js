import React from 'react';
import {useTranslation} from "react-i18next";
import {Row, Col, Divider} from 'antd';

function GenericNotification({target, status, buttons}) {
    return (
        <div className={"request-notification"}>
            <Row>
                <Col span={12}>
                    <h3>{target}</h3>
                </Col>
                <Col span={7}>
                    <h3>{status}</h3>
                </Col>
                <Col>
                    <h3>{buttons}</h3>
                </Col>
            </Row>
        </div>
    )
}

export default GenericNotification;
