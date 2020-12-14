import React from 'react';
import {Row, Col} from 'antd';

function GenericNotification({target, status, buttons, shaded}) {
    let classes = null;
    if (shaded) {
        classes = "request-notification shaded-div"
    } else {
        classes = "request-notification"
    }


    return (
        <div className={classes}>
            <Row>
                <Col span={12}>
                    <h3>{target}</h3>
                </Col>
                <Col span={4}>
                    <h3>{status}</h3>
                </Col>
                <Col span={8}>
                    <div className={"centered"}>
                        <h3>{buttons}</h3>
                    </div>
                </Col>

            </Row>
        </div>
    )
}

export default GenericNotification;
