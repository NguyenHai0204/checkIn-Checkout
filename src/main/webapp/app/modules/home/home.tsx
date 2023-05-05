import './home.scss';

import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  const [time, setTime] = useState(new Date());

  useEffect(() => {
    setInterval(() => {
      setTime(new Date());
    }, 1000);
  }, []);

  const checkIn = timeSheetData => {
    axios
      .post('/api/time-sheets/checkin', timeSheetData)
      .then(response => {
        console.log(response.status);
        if (response.status === 201) {
          toast.success('Check In Thành Công!');
        }
      })
      .catch(error => {
        toast.error('Check In Thất Bại!!!');
        console.error(error);
      });
  };

  const checkout = timeSheetData => {
    axios
      .post('/api/time-sheets/checkout', timeSheetData)
      .then(response => {
        console.log(response.status);
        if (response.status === 201) {
          toast.success('Check Out Thành Công!');
        }
      })
      .catch(error => {
        toast.error('Check Out Thất Bại!!!');
        console.error(error);
      });
  };

  const handleSubmitCheckin = event => {
    event.preventDefault();
    const timeSheetData = {
      user: 'HaiNguyen',
      // Thêm các thuộc tính khác của TimeSheet vào đây
    };
    checkIn(timeSheetData);
  };

  const handleSubmitCheckout = event => {
    event.preventDefault();
    const timeSheetData = {
      user: 'HaiNguyen',
      // Thêm các thuộc tính khác của TimeSheet vào đây
    };
    checkout(timeSheetData);
  };

  return (
    <Row>
      <Col md="12">
        {account?.login ? (
          <div>
            <h1 id="title">Check In / Check Out</h1>
            <h2 id="time">{time.toLocaleTimeString()}</h2>
            <h5 id="date">
              Ngày {time.getDate()}, Tháng {time.getMonth() + 1}, Năm {time.getFullYear()}
            </h5>
            <div id="button">
              <button onClick={handleSubmitCheckin} className="button">
                Check In
              </button>
              <button onClick={handleSubmitCheckout} className="button">
                Check Out
              </button>
            </div>
            <div id="body">
              <table id="timeSheetDaily">
                <tr>
                  <th>Ngày</th>
                  <th>User</th>
                  <th>Check In</th>
                  <th>Check Out</th>
                </tr>
                <tr>
                  <td>04/05/2023</td>
                  <td>archer@vietnam.vn</td>
                  <td>08:00:00 AM</td>
                  <td>05:00:00 PM</td>
                </tr>
              </table>
            </div>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <Link to="/login" className="alert-link">
                sign in
              </Link>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;) <br />- User (login=&quot;user&quot; and
              password=&quot;user&quot;).
            </Alert>

            <Alert color="warning">
              You don&apos;t have an account yet?&nbsp;
              <Link to="/account/register" className="alert-link">
                Register a new account
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
