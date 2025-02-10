import React from 'react';
import "../styles/styles.css"

const HintField = ({ indexMatrix, formattedColorPalette }) => {
	return (
		<div className="hint-field">
			{indexMatrix.map((row, rowIndex) => (
				<div key={rowIndex} className="hint-row">
					{row.map((colorIndex, colIndex) => (
						<div
							key={colIndex}
							className="hint-cell"
							style={{
								backgroundColor: `rgb(
					${formattedColorPalette[colorIndex][0]},
					${formattedColorPalette[colorIndex][1]},
					${formattedColorPalette[colorIndex][2]})`
							}}
						></div>
					))}
				</div>
			))}
		</div>
	);
};

export default HintField;