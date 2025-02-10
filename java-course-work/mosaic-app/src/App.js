// App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import LeaderboardPage from './pages/LeaderboardPage';
import CreateMosaicPage from './pages/CreateMosaicPage';
import InformationPage from './pages/InformationPage';
import GamePage from './pages/GamePage';

function App() {
  return (
    <Router basename={"/mosaic"}>
      <Routes>
        <Route exact path="/" element={<HomePage />} />
        <Route path="/leaderboard" element={<LeaderboardPage />} />
        <Route path="/create" element={<CreateMosaicPage />} />
        <Route path="/information" element={<InformationPage />} />
        <Route path="/game/:mosaicId" element={<GamePage />} />
      </Routes>
    </Router>
  );
}

export default App;
