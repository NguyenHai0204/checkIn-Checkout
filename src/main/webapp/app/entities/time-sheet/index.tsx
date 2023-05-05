import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TimeSheet from './time-sheet';
import TimeSheetDetail from './time-sheet-detail';
import TimeSheetUpdate from './time-sheet-update';
import TimeSheetDeleteDialog from './time-sheet-delete-dialog';

const TimeSheetRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TimeSheet />} />
    <Route path="new" element={<TimeSheetUpdate />} />
    <Route path=":id">
      <Route index element={<TimeSheetDetail />} />
      <Route path="edit" element={<TimeSheetUpdate />} />
      <Route path="delete" element={<TimeSheetDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TimeSheetRoutes;
