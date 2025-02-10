// HomePage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import Navbar from '../components/Navbar';
import MosaicCard from '../components/MosaicCard';
import '../styles/styles.css';
let pseuso_json = require("../pseudo-env.json");

const HomePage = () => {
  const [records, setRecords] = useState([]);
  const [mosaics, setMosaics] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetch(pseuso_json['api-host'] + '/api/mosaic')
      .then(response => response.json())
      .then(data => setMosaics(data))
      .catch(error => console.error('Error fetching mosaics:', error));

    fetch(pseuso_json['api-host'] + '/api/record')
      .then(response => response.json())
      .then(data => setRecords(data))
      .catch(error => console.error('Error fetching records:', error));
  }, []);

  const handleCardClick = (mosaicId) => {
    navigate(`/game/${mosaicId}`);
  };

  return (
    <div>
      <Navbar />
      {/* <h1>Home Page</h1> */}
      <h1>Галерея</h1>
      <div className="mosaics-container">
        {mosaics.map(mosaic => (
          <div key={mosaic.id} onClick={() => handleCardClick(mosaic.id)}>
            <MosaicCard mosaic={mosaic} records={records} />
          </div>
        ))}

      </div>
    </div >
  );
};

export default HomePage;
