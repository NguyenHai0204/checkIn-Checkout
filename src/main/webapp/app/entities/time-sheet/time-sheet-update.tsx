import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { getEntity, updateEntity, createEntity, reset } from './time-sheet.reducer';

export const TimeSheetUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const timeSheetEntity = useAppSelector(state => state.timeSheet.entity);
  const loading = useAppSelector(state => state.timeSheet.loading);
  const updating = useAppSelector(state => state.timeSheet.updating);
  const updateSuccess = useAppSelector(state => state.timeSheet.updateSuccess);

  const handleClose = () => {
    navigate('/time-sheet');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.checkIn = convertDateTimeToServer(values.checkIn);
    values.checkOut = convertDateTimeToServer(values.checkOut);
    values.overTime = convertDateTimeToServer(values.overTime);

    const entity = {
      ...timeSheetEntity,
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
          checkIn: displayDefaultDateTime(),
          checkOut: displayDefaultDateTime(),
          overTime: displayDefaultDateTime(),
        }
      : {
          ...timeSheetEntity,
          checkIn: convertDateTimeFromServer(timeSheetEntity.checkIn),
          checkOut: convertDateTimeFromServer(timeSheetEntity.checkOut),
          overTime: convertDateTimeFromServer(timeSheetEntity.overTime),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="checkinCheckoutApp.timeSheet.home.createOrEditLabel" data-cy="TimeSheetCreateUpdateHeading">
            Create or edit a Time Sheet
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="time-sheet-id" label="Id" validate={{ required: true }} /> : null}
              <ValidatedField label="Date" id="time-sheet-date" name="date" data-cy="date" type="date" />
              <ValidatedField
                label="Check In"
                id="time-sheet-checkIn"
                name="checkIn"
                data-cy="checkIn"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Check Out"
                id="time-sheet-checkOut"
                name="checkOut"
                data-cy="checkOut"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label="Over Time"
                id="time-sheet-overTime"
                name="overTime"
                data-cy="overTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField label="User" id="time-sheet-user" name="user" data-cy="user" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/time-sheet" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TimeSheetUpdate;
