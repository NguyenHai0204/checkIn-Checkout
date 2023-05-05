import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './time-sheet.reducer';

export const TimeSheetDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timeSheetEntity = useAppSelector(state => state.timeSheet.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timeSheetDetailsHeading">Time Sheet</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">Id</span>
          </dt>
          <dd>{timeSheetEntity.id}</dd>
          <dt>
            <span id="date">Date</span>
          </dt>
          <dd>{timeSheetEntity.date ? <TextFormat value={timeSheetEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="checkIn">Check In</span>
          </dt>
          <dd>{timeSheetEntity.checkIn ? <TextFormat value={timeSheetEntity.checkIn} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="checkOut">Check Out</span>
          </dt>
          <dd>{timeSheetEntity.checkOut ? <TextFormat value={timeSheetEntity.checkOut} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="overTime">Over Time</span>
          </dt>
          <dd>{timeSheetEntity.overTime ? <TextFormat value={timeSheetEntity.overTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="user">User</span>
          </dt>
          <dd>{timeSheetEntity.user}</dd>
        </dl>
        <Button tag={Link} to="/time-sheet" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/time-sheet/${timeSheetEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimeSheetDetail;
