import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './employee.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EmployeeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  const loading = useAppSelector(state => state.employee.loading);
  const updating = useAppSelector(state => state.employee.updating);
  const updateSuccess = useAppSelector(state => state.employee.updateSuccess);
  const handleClose = () => {
    props.history.push('/employee' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.empBirthday = convertDateTimeToServer(values.empBirthday);
    values.empJoinDate = convertDateTimeToServer(values.empJoinDate);

    const entity = {
      ...employeeEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          empBirthday: displayDefaultDateTime(),
          empJoinDate: displayDefaultDateTime(),
        }
      : {
          ...employeeEntity,
          empBirthday: convertDateTimeFromServer(employeeEntity.empBirthday),
          empJoinDate: convertDateTimeFromServer(employeeEntity.empJoinDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="springDemoApp.employee.home.createOrEditLabel" data-cy="EmployeeCreateUpdateHeading">
            <Translate contentKey="springDemoApp.employee.home.createOrEditLabel">Create or edit a Employee</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="employee-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('springDemoApp.employee.empName')}
                id="employee-empName"
                name="empName"
                data-cy="empName"
                type="text"
              />
              <ValidatedField
                label={translate('springDemoApp.employee.empBirthday')}
                id="employee-empBirthday"
                name="empBirthday"
                data-cy="empBirthday"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('springDemoApp.employee.empCode')}
                id="employee-empCode"
                name="empCode"
                data-cy="empCode"
                type="text"
              />
              <ValidatedField
                label={translate('springDemoApp.employee.empGender')}
                id="employee-empGender"
                name="empGender"
                data-cy="empGender"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('springDemoApp.employee.empEmail')}
                id="employee-empEmail"
                name="empEmail"
                data-cy="empEmail"
                type="text"
              />
              <ValidatedField
                label={translate('springDemoApp.employee.empJoinDate')}
                id="employee-empJoinDate"
                name="empJoinDate"
                data-cy="empJoinDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/employee" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default EmployeeUpdate;
