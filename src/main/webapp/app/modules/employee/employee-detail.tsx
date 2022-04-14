import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './employee.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const EmployeeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const employeeEntity = useAppSelector(state => state.employee.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="employeeDetailsHeading">
          <Translate contentKey="springDemoApp.employee.detail.title">Employee</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.id}</dd>
          <dt>
            <span id="empName">
              <Translate contentKey="springDemoApp.employee.empName">Emp Name</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.empName}</dd>
          <dt>
            <span id="empBirthday">
              <Translate contentKey="springDemoApp.employee.empBirthday">Emp Birthday</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.empBirthday ? (
              <TextFormat value={employeeEntity.empBirthday} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="empCode">
              <Translate contentKey="springDemoApp.employee.empCode">Emp Code</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.empCode}</dd>
          <dt>
            <span id="empGender">
              <Translate contentKey="springDemoApp.employee.empGender">Emp Gender</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.empGender ? 'true' : 'false'}</dd>
          <dt>
            <span id="empEmail">
              <Translate contentKey="springDemoApp.employee.empEmail">Emp Email</Translate>
            </span>
          </dt>
          <dd>{employeeEntity.empEmail}</dd>
          <dt>
            <span id="empJoinDate">
              <Translate contentKey="springDemoApp.employee.empJoinDate">Emp Join Date</Translate>
            </span>
          </dt>
          <dd>
            {employeeEntity.empJoinDate ? <TextFormat value={employeeEntity.empJoinDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/employee" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/employee/${employeeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EmployeeDetail;
