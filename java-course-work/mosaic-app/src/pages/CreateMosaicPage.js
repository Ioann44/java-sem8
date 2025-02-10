// CreateMosaicPage.js
import React, { useState } from 'react';

import Navbar from '../components/Navbar';
const pseudo_env = require('../pseudo-env.json');

const CreateMosaicPage = () => {
  const [imageFile, setImageFile] = useState(null);
  const [rows, setRows] = useState('');
  const [cols, setCols] = useState('');

  const handleImageChange = (e) => {
    setImageFile(e.target.files[0]);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!imageFile || !rows || !cols) {
      alert('Please fill in all fields');
      return;
    }

    // Преобразование файла изображения в строку Base64
    const reader = new FileReader();
    reader.readAsDataURL(imageFile);
    const base64Image = reader.result;

    const formData = new FormData();
    formData.append('imageFile', base64Image); // Отправляем строку Base64
    formData.append('rows', rows);
    formData.append('cols', cols);

    try {
      const response = await fetch(pseudo_env['api-host'] + '/api/mosaic', {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        console.log('Mosaic created successfully');
        alert('Mosaic created successfully!');
        // Очистить форму после успешного создания мозаики
        // setImageFile(null);
        // setRows('');
        // setCols('');
      } else {
        throw new Error('Failed to create mosaic');
      }
    } catch (error) {
      console.error('Error creating mosaic:', error);
      alert('Failed to create mosaic. Please try again.');
    }
  };

  return (
    <div>
      <Navbar />
      <h1>Create Mosaic Page</h1>
      <div>
        <div>
          <h2>Create a New Mosaic</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <label htmlFor="imageFile">Upload Image:</label>
              <input
                type="file"
                id="imageFile"
                accept="image/*"
                onChange={handleImageChange}
                required
              />
            </div>
            <div>
              <label htmlFor="rows">Rows:</label>
              <input
                type="number"
                id="rows"
                value={rows}
                onChange={(e) => setRows(e.target.value)}
                required
              />
            </div>
            <div>
              <label htmlFor="cols">Columns:</label>
              <input
                type="number"
                id="cols"
                value={cols}
                onChange={(e) => setCols(e.target.value)}
                required
              />
            </div>
            <button type="submit">Create Mosaic</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CreateMosaicPage;
