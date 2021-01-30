import React, { useContext } from "react";
import { useTranslation } from "react-i18next";
import { ErrorMessage, Formik } from "formik";
import {
    Form,
    Input,
    InputNumber,
    Checkbox,
    Select,
    DatePicker,
} from "formik-antd";
import { Button, Spin, Upload } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import * as Yup from "yup";
import ConstantsContext from "../../constants/constantsContext";
import DeleteImagesInput from "../editPet/DeleteImagesInput";
import "../../css/addPet/addPetForm.css";

const FormItem = Form.Item;

const SUPPORTED_FORMATS = ["image/jpg", "image/jpeg"];

const typeCheck = (val) => {
    let bool = true;
    for (let i = 0; i < val.length; i++) {
        bool = bool && SUPPORTED_FORMATS.includes(val[i].type);
    }
    return bool;
};

const defaultValues = {
    petName: "",
    price: "",
    isAdoption: "",
    description: "",
    province: "",
    department: "",
    species: "",
    breed: "",
    dateOfBirth: "",
    isVaccinated: "",
    gender: "",
    filesToDelete: [],
    files: [],
};

function AddPetForm({ submitting, onSubmit, editing, initialValues }) {
    const { species, breeds, provinces, departments } = useContext(
        ConstantsContext
    );
    const { t } = useTranslation("addPet");

    const _onSubmit = (values) => {
        onSubmit(values);
    };

    if (editing && !initialValues.petName) return <Spin />;

    return (
        <Formik
            validationSchema={Yup.object().shape({
                petName: Yup.string().required(t("form.petName.required")),
                price: Yup.number().when("isAdoption", {
                    is: true,
                    otherwise: Yup.number()
                        .typeError(t("form.price.required"))
                        .required(t("form.price.required")),
                    then: Yup.number().typeError(t("form.price.required")),
                }),
                isAdoption: Yup.boolean(),
                description: Yup.string().required(
                    t("form.description.required")
                ),
                province: Yup.number().required(t("form.province.required")),
                department: Yup.number().required(
                    t("form.department.required")
                ),
                species: Yup.number().required(t("form.species.required")),
                breed: Yup.number().required(t("form.breed.required")),
                dateOfBirth: Yup.date().required(
                    t("form.dateOfBirth.required")
                ),
                isVaccinated: Yup.boolean(),
                gender: Yup.string().required(t("form.gender.required")),
                files: editing
                    ? Yup.array().test(
                          "fileType",
                          t("form.images.errorType"),
                          (value) => typeCheck(value)
                      )
                    : Yup.array()
                          .min(1, (min) => t("form.images.min", { min }))
                          .test(
                              "fileType",
                              t("form.images.errorType"),
                              (value) => typeCheck(value)
                          ),
                filesToDelete: Yup.array().when("files", {
                    is: (files) => editing && files.length === 0,
                    then: Yup.array().max(
                        initialValues && initialValues.images.length - 1,
                        t("form.filesToDelete.atLeastOneImage")
                    ),
                }),
            })}
            onSubmit={_onSubmit}
            initialValues={Object.assign({}, defaultValues, initialValues)}
            render={({ setFieldValue, values }) => {
                return (
                    <Form>
                        <div className={"add-pet-form"}>
                            <div className={"add-pet-form__group"}>
                                <FormItem
                                    name={"petName"}
                                    label={t("form.petName.label")}
                                >
                                    <Input
                                        name={"petName"}
                                        placeholder={t(
                                            "form.petName.placeholder"
                                        )}
                                    />
                                </FormItem>

                                <FormItem
                                    name={"price"}
                                    label={t("form.price.label")}
                                >
                                    <InputNumber
                                        name={"price"}
                                        placeholder={t(
                                            "form.price.placeholder"
                                        )}
                                        disabled={values.isAdoption}
                                        min={0}
                                    />
                                </FormItem>

                                <FormItem
                                    name={"isAdoption"}
                                    label={t("form.isAdoption.label")}
                                >
                                    <Checkbox
                                        name={"isAdoption"}
                                        onChange={() =>
                                            setFieldValue("price", 0)
                                        }
                                    />
                                </FormItem>

                                <FormItem
                                    name={"description"}
                                    label={t("form.description.label")}
                                >
                                    <Input
                                        name={"description"}
                                        placeholder={t(
                                            "form.description.placeholder"
                                        )}
                                    />
                                </FormItem>
                            </div>

                            <div className={"add-pet-form__group"}>
                                <FormItem
                                    name={"province"}
                                    label={t("form.province.label")}
                                >
                                    <Select
                                        name={"province"}
                                        placeholder={t(
                                            "form.province.placeholder"
                                        )}
                                        onChange={() =>
                                            setFieldValue("department", "")
                                        }
                                    >
                                        {provinces &&
                                            Object.values(provinces).map(
                                                ({ id, name }) => {
                                                    return (
                                                        <Select.Option
                                                            value={id}
                                                        >
                                                            {name}
                                                        </Select.Option>
                                                    );
                                                }
                                            )}
                                    </Select>
                                </FormItem>

                                <FormItem
                                    name={"department"}
                                    label={t("form.department.label")}
                                >
                                    <Select
                                        name={"department"}
                                        placeholder={t(
                                            "form.department.placeholder"
                                        )}
                                        disabled={!values.province}
                                    >
                                        {values.province &&
                                            provinces &&
                                            provinces[
                                                values.province
                                            ].departmentIds.map(
                                                (departmentId) => {
                                                    const {
                                                        id,
                                                        name,
                                                    } = departments[
                                                        departmentId
                                                    ];

                                                    return (
                                                        <Select.Option
                                                            value={id}
                                                        >
                                                            {name}
                                                        </Select.Option>
                                                    );
                                                }
                                            )}
                                    </Select>
                                </FormItem>

                                <FormItem
                                    name={"species"}
                                    label={t("form.species.label")}
                                >
                                    <Select
                                        name={"species"}
                                        placeholder={t(
                                            "form.species.placeholder"
                                        )}
                                        onChange={() =>
                                            setFieldValue("breed", "")
                                        }
                                    >
                                        {species &&
                                            Object.values(species).map(
                                                ({ id, name }) => {
                                                    return (
                                                        <Select.Option
                                                            value={id}
                                                        >
                                                            {name}
                                                        </Select.Option>
                                                    );
                                                }
                                            )}
                                    </Select>
                                </FormItem>

                                <FormItem
                                    name={"breed"}
                                    label={t("form.breed.label")}
                                >
                                    <Select
                                        name={"breed"}
                                        placeholder={t(
                                            "form.breed.placeholder"
                                        )}
                                        disabled={!values.species}
                                    >
                                        {values.species &&
                                            species &&
                                            species[
                                                values.species
                                            ].breedIds.map((breedId) => {
                                                const { id, name } = breeds[
                                                    breedId
                                                ];

                                                return (
                                                    <Select.Option value={id}>
                                                        {name}
                                                    </Select.Option>
                                                );
                                            })}
                                        <Select.Option value={"breed"}>
                                            breed
                                        </Select.Option>
                                    </Select>
                                </FormItem>
                            </div>

                            <div className={"add-pet-form__group"}>
                                <FormItem
                                    name={"dateOfBirth"}
                                    label={t("form.dateOfBirth.label")}
                                >
                                    {/*TODO: localizar*/}
                                    <DatePicker name={"dateOfBirth"} />
                                </FormItem>

                                <FormItem
                                    name={"isVaccinated"}
                                    label={t("form.isVaccinated.label")}
                                >
                                    <Checkbox name={"isVaccinated"} />
                                </FormItem>

                                <FormItem
                                    name={"gender"}
                                    label={t("form.gender.label")}
                                >
                                    <Select
                                        name={"gender"}
                                        placeholder={t(
                                            "form.gender.placeholder"
                                        )}
                                    >
                                        <Select.Option value={"male"}>
                                            {t("form.gender.male")}
                                        </Select.Option>
                                        <Select.Option value={"female"}>
                                            {t("form.gender.female")}
                                        </Select.Option>
                                    </Select>
                                </FormItem>
                            </div>
                        </div>

                        <div>
                            {editing && (
                                <>
                                    <DeleteImagesInput
                                        setFieldValue={setFieldValue}
                                        values={values}
                                    />
                                    <p className={"error-message"}>
                                        <ErrorMessage name={"filesToDelete"} />
                                    </p>
                                </>
                            )}

                            <Upload
                                beforeUpload={() => false}
                                listType="picture-card"
                                onChange={({ fileList }) => {
                                    setFieldValue("files", [...fileList]);
                                }}
                            >
                                <div>
                                    <PlusOutlined />
                                    <div style={{ marginTop: 8 }}>
                                        {t("form.upload")}
                                    </div>
                                </div>
                            </Upload>
                            <p className={"error-message"}>
                                <ErrorMessage name={"files"} />
                            </p>
                        </div>

                        <FormItem name>
                            <Button
                                type="primary"
                                htmlType="submit"
                                loading={submitting}
                            >
                                {t("form.send")}
                            </Button>
                        </FormItem>
                    </Form>
                );
            }}
        />
    );
}

export default AddPetForm;
