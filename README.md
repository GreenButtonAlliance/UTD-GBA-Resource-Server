[![CircleCI](https://dl.circleci.com/status-badge/img/gh/GreenButtonAlliance/GBA-Resource-Server/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/GreenButtonAlliance/GBA-Resource-Server/tree/main)

# GBA Resource Server Project

The GBA Resource Server is a project to replace and upgrade the existing Green Button Alliance Data Custodian
Sandbox application. The upgrade will include the following objectives:

- Separate the Sandbox Authorization and Resource Server Functionality
- Add support for the Retail Customer schema feature of the "NAESB REQ.21 ESPI ver. 3.3" standard
- Simplify the GBA Resource Server CRUD interface by only supporting GET requests
- Remap the current Energy Usage database to improve performance
- Provide JUnit and Integration tests scripts
- Provide support for Docker and test containers

The original source code can be found in two GitHub repositories:

- [OpenESPI-Common](https://github.com/greenbuttonalliance/OpenESPI-Common-java)
- [OpenESPI-DataCustodian](https://github.com/greenbuttonalliance/OpenESPI-DataCustodian-java)

## OpenESPI-Common

This repository contains run-time and test code (no longer used) to support the original energy usage database and atom
domains, repositories, and service methods. Only the Energy Usage database exist in this repository.

## OpenESPI-DataCustodian

This repository contains run-time and test code (no longer used) to support API logic to extract data from the energy
usage database.

## Project Resources

- Copies of the NAESB REQ.21 ESPI ver. 3.3 standard can be obtained for evaluation by contacting the North American
  Energy Standards Board (naesb.org).
- Copies of the NAESB REQ.21 ESPI ver. 3.3 Energy Usage and Retail Customer schemas can be found in the "schemas"
  directory. The NAESB copyright prevents the use of the copyrighted schema file names. Therefore, the "usage.xsd"
  schema contains the NAESB Energy Usage (i.e., espi.xsd) and the "retailcustomer.xsd" schema contains the NAESB Retail
  Customer (i.e., customer.xsd).
- The "atom.xsd" schema was developed under a NIST Grant and uses the Apache License 2.0 open source license.

## Best Practices

- All developed code must be tested using best-of-practice testing methodologies
- All Pull Request (PR) will be reviewed prior to inclusion in the project repository.
