import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT, APP_LOCAL_TIME_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITimeSheet } from 'app/shared/model/time-sheet.model';
import { getEntities } from './time-sheet.reducer';

export const TimeSheet = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const timeSheetList = useAppSelector(state => state.timeSheet.entities);
  const loading = useAppSelector(state => state.timeSheet.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="time-sheet-heading" data-cy="TimeSheetHeading">
        Time Sheets
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/time-sheet/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Time Sheet
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {timeSheetList && timeSheetList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>Id</th>
                <th>Date</th>
                <th>Check In</th>
                <th>Check Out</th>
                <th>Over Time</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {timeSheetList.map((timeSheet, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/time-sheet/${timeSheet.id}`} color="link" size="sm">
                      {timeSheet.id}
                    </Button>
                  </td>
                  <td>{timeSheet.date ? <TextFormat type="date" value={timeSheet.date} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{timeSheet.checkIn ? <TextFormat type="date" value={timeSheet.checkIn} format={APP_LOCAL_TIME_FORMAT} /> : null}</td>
                  <td>
                    {timeSheet.checkOut ? <TextFormat type="date" value={timeSheet.checkOut} format={APP_LOCAL_TIME_FORMAT} /> : null}
                  </td>
                  <td>
                    {timeSheet.overTime ? <TextFormat type="date" value={timeSheet.overTime} format={APP_LOCAL_TIME_FORMAT} /> : null}
                  </td>
                  <td>{timeSheet.user}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/time-sheet/${timeSheet.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/time-sheet/${timeSheet.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/time-sheet/${timeSheet.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Time Sheets found</div>
        )}
      </div>
    </div>
  );
};

export default TimeSheet;
