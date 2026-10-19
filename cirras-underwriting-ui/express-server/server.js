const express = require('express');
const http = require('http');
const path = require('path');
const cors = require('cors');
const fs = require('fs');

const app = express();

// Host setup
const port = process.env.TOMCAT_PORT || 8080;
const hostname = "0.0.0.0";

const angularPath = path.join(__dirname, '../angular/dist/cirras-underwriting');

function startServer() {
	try {

		console.log(`Updating appConfig.json`);

		const configPath = path.join(angularPath, '/assets/data/appConfig.json');

		// Read the template and perform replacement in memory safely at startup
		let configTemplate = fs.readFileSync(configPath, 'utf8');
		let processedConfig = configTemplate
			.replace(/#\{BASE_URL\}#/g, process.env.BASE_URL)
			.replace(/#\{APPLICATION_ENVIRONMENT_NAME\}#/g, process.env.APPLICATION_ENVIRONMENT_NAME )
			.replace(/#\{CIRRAS_UNDERWRITING_REST_URI\}#/g, process.env.CIRRAS_UNDERWRITING_REST_URI)
			.replace(/#\{GRAIN_VERIFIED_YIELD_CALCULATOR_URI\}#/g, process.env.GRAIN_VERIFIED_YIELD_CALCULATOR_URI)
			.replace(/#\{FORAGE_VERIFIED_YIELD_CALCULATOR_URI\}#/g, process.env.FORAGE_VERIFIED_YIELD_CALCULATOR_URI)
			.replace(/#\{WEBADE_OAUTH2_AUTHORIZE_URL\}#/g, process.env.WEBADE_OAUTH2_AUTHORIZE_URL)
			.replace(/#\{WEBADE_OAUTH2_SCOPES\}#/g, process.env.WEBADE_OAUTH2_SCOPES)
			.replace(/#\{WEBADE_OAUTH2_ENABLE_CHECKTOKEN\}#/g, process.env.WEBADE_OAUTH2_ENABLE_CHECKTOKEN )
			.replace(/#\{UI_CHECKTOKEN_ENDPOINT\}#/g, process.env.UI_CHECKTOKEN_ENDPOINT )
			.replace(/#\{WEBADE_OAUTH2_SITEMINDER_URL\}#/g, process.env.WEBADE_OAUTH2_SITEMINDER_URL );

		// Middleware
		app.use(cors());
		app.use(express.json());

		// Serve the modified config dynamically when requested
		console.log(`Serve the modified config dynamically`);
		app.get('/pub/cirras-underwriting/assets/data/appConfig.json', (req, res) => {
			res.setHeader('Content-Type', 'application/json');
			res.send(processedConfig);
		});

		app.use('/pub/cirras-underwriting', express.static(angularPath));

		// Send all requests to Angular app
		app.get('/pub/cirras-underwriting/*', (req, res) => {
			res.sendFile(path.join(angularPath, 'index.html'));
		});

		const server = http.createServer(app); 
		
		// Bind to a Specific IP / Hostname (Crucial for Docker/Clouds)
		server.listen(port, hostname, () => {
			console.log(`angular app running on http://${hostname}:${port}`);
		});
		
	} catch (error) {
		console.error('Failed to initialize server:', error.message);
		// Exit process with failure status code so Docker detects container failure
		process.exit(1);
	};
};

startServer();


