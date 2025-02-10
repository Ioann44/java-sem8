const MosaicCard = ({ mosaic, records }) => {
	const { id, imageBase64, rows, cols } = mosaic;

	// Фильтрация и выбор 3 лучших рекордов для текущей мозаики
	const top3Records = records
		.filter(record => record.mosaicId === id)
		.sort((a, b) => a.time - b.time)
		.slice(0, 3);

	return (
		<div className="mosaic-card">
			<img className="mosaic-image" src={imageBase64} alt={`Mosaic ${id}`} />
			<div className="mosaic-details">
				<p>Строк: {rows}, Столбцов: {cols}</p>
				<div className="top-records">
					<h3>Рекорды:</h3>
					{top3Records.map((record, index) => (
						<p key={index}>{record.name}: {record.time / 10} seconds</p>
					))}
				</div>
			</div>
		</div>
	);
};

export default MosaicCard;