import React, { useState, useRef, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

import HintField from "../components/HintField";
import Navbar from "../components/Navbar";
import '../styles/styles.css';
let pseuso_env = require("../pseudo-env.json");
var formattedColorPalette = [[255, 255, 255]];
var mosaicIdValue = 0;

const sendRecordToServer = async (userName, mosaicId, gameDuration) => {
	const response = await fetch(pseuso_env['api-host'] + `/api/record?name=${userName}&time=${(gameDuration * 10).toFixed(0)}&mosaicId=${mosaicId}`, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
		}
	});

	if (!response.ok) {
		throw new Error('Ошибка при отправке рекорда');
	}

	return response.json();
};

const GamePage = () => {
	const navigate = useNavigate();
	const { mosaicId } = useParams();
	const [mosaicData, setMosaicData] = useState(null);
	const [selectedColor, setSelectedColor] = useState([255, 255, 255]);
	const [gameTime, setGameTime] = useState(0);
	const [gameIsRunning, setGameIsRunning] = useState(false);
	const gameIsRunningRef = useRef(gameIsRunning);
	gameIsRunningRef.current = gameIsRunning;
	const [gameField, setGameField] = useState([]);
	const [indexMatrix, setIndexMatrix] = useState([]);
	const [indexMatrixFlattened, setIndexMatrixFlattened] = useState([]);
	const [weWin, setWeWin] = useState(false);
	const [mouseIsDown, setMouseIsDown] = useState(false);

	useEffect(() => {
		mosaicIdValue = mosaicId;
		fetch(pseuso_env['api-host'] + `/api/mosaic/${mosaicId}`)
			.then(response => response.json())
			.then(data => {
				formattedColorPalette = data.colorPalette.reduce((acc, value, index) => {
					if (index % 3 === 0) {
						acc.push([value]);
					} else {
						acc[acc.length - 1].push(value);
					}
					return acc;
				}, []);
				let indexMatrixArr = data.indexMatrix.reduce((acc, value, index) => {
					if (index % data.cols === 0) {
						acc.push([value]);
					} else {
						acc[acc.length - 1].push(value);
					}
					return acc;
				}, []);
				setIndexMatrixFlattened(data.indexMatrix);
				setIndexMatrix(indexMatrixArr);
				setMosaicData(data);
				// Инициализация игрового поля
				const initialField = Array(data.rows * data.cols).fill([255, 255, 255]);
				setGameField(initialField);
				setInterval(() => { if (gameIsRunningRef.current) setGameTime(gameTime => gameTime + 0.1); }, 100);
			})
			.catch(error => console.error('Error fetching mosaic data:', error));

		onmousedown = (event) => { setMouseIsDown(true) };
		onmouseup = (event) => { setMouseIsDown(false) };
	}, [mosaicId]);

	const handleCellClick = (index) => {
		if (!gameIsRunning) return

		// Логика изменения цвета ячейки
		const newGameField = [...gameField];
		newGameField[index] = selectedColor;
		setGameField(newGameField);

		if (JSON.stringify(newGameField) === JSON.stringify(indexMatrixFlattened.map(value => { return formattedColorPalette[value]; }))) {
			setGameIsRunning(false);

			const userName = window.prompt(`Ваш результат: ${gameTime.toFixed(1)} с\nВведите имя для записи в рекорды`);
			const gameDuration = gameTime;

			setWeWin(true);

			sendRecordToServer(userName, mosaicIdValue, gameDuration)
				.then(() => {
					navigate('/');
				}).catch(
					(error) => { console.error("Ошибка при отправке рекорда:", error); }
				);
		}
	};

	const handleColorSelect = (color) => {
		setSelectedColor(color);
	};

	if (!mosaicData) {
		return <div>Loading...</div>;
	} else if (weWin) {
		return (
			<div>
				<Navbar />
				<div style={{ display: 'flex', alignItems: 'center', justifyContent: "center" }}>
					<img src={mosaicData.imageBase64} alt="Mosaic" style={{ display: 'block', maxWidth: '80%', maxHeight: '80%' }} />
				</div>
			</div>
		);
	}

	return (
		<div>
			<Navbar />
			<div>
				<HintField indexMatrix={indexMatrix} formattedColorPalette={formattedColorPalette} />
				<div className={'game-field'}>
					{Array.from({ length: mosaicData.rows }).map((_, rowIndex) => (
						<div key={rowIndex} className={'game-row'}>
							{Array.from({ length: mosaicData.cols }).map((_, colIndex) => {
								const index = rowIndex * mosaicData.cols + colIndex;
								return (
									<div
										key={index}
										className={'game-cell'}
										style={{
											backgroundColor: `rgb(
										${gameField[index][0]},
										${gameField[index][1]},
										${gameField[index][2]})`
										}}
										onMouseMove={() => { if (mouseIsDown) handleCellClick(index); }}
										onMouseDown={() => handleCellClick(index)}
										onDragStart={(event) => { event.preventDefault(); }}
									></div>
								);
							})}
						</div>
					))}
				</div>
				<div className="color-palette">
					{formattedColorPalette.map((color, index) => (
						<div
							key={index}
							className="color-palette-item"
							style={{ backgroundColor: `rgb(${color[0]}, ${color[1]}, ${color[2]})` }}
							onClick={() => handleColorSelect(color)}
						></div>
					))}
				</div>
				<div className='timer'>Время: {`${gameTime.toFixed(1)}`}</div>
				<button className='buttonStart' onClick={() => { if (gameTime === 0) setGameIsRunning(true); }}>Начать</button>
			</div >
		</div>
	);
};

export default GamePage;
