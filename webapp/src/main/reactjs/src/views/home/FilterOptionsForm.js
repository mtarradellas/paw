import React from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";

const FormItem = Form.Item;

const FilterOptionsForm = () => {
    return <Form layout={"vertical"}>
            <div className={"form-content"}>
                <FormItem name={"specie"} label={"Specie"}>
                    <Select name={"specie"}>
                        <Select.Option value={"cat"}>Cat</Select.Option>
                        <Select.Option value={"dog"}>Dog</Select.Option>
                    </Select>
                </FormItem>

                <FormItem name={"breed"} label={"Breed"}>
                    <Select name={"breed"}>

                    </Select>
                </FormItem>

                <FormItem name={"price"} label={"Price"}>
                    <Select name={"price"}>

                    </Select>
                </FormItem>

                <FormItem name={"sex"} label={"Sex"}>
                    <Select name={"sex"}>

                    </Select>
                </FormItem>

                <FormItem name={"state"} label={"State"}>
                    <Select name={"state"}>

                    </Select>
                </FormItem>

                <FormItem name={"state"} label={"State"}>
                    <Select name={"state"}>

                    </Select>
                </FormItem>

                <FormItem name={"criteria"} label={"Criteria"}>
                    <Select name={"criteria"}>

                    </Select>
                </FormItem>

                <FormItem name={"order"} label={"order"}>
                    <Select name={"order"}>

                    </Select>
                </FormItem>
            </div>

            <div className={"form-buttons"}>
                <Button type={"primary"} htmlType={"submit"}>Filter</Button>

                <Button type={"secondary"}>Clear</Button>
            </div>
        </Form>
}

export default withFormik({
    mapPropsToValues: () => ({}),
    handleSubmit: (values) => {
        console.log(values);
    }
})(FilterOptionsForm);