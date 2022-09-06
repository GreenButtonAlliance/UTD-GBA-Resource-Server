# GBA Resource Server (UTD FA2022 CS4885 Project)
The GBA Resource Server is a project to replace the original Sample North American Energy Standards Board (NAESB) Energy Service Provider Interface (ESPI) standard sample Utility OAuth 2.0 Authorization Server and Resource Server application.  The original source code can be found in two GitHub repositories:

- [OpenESPI-Common](https://github.com/greenbuttonalliance/OpenESPI-Common-java) 

- [OpenESPI-DataCustodian](https://github.com/greenbuttonalliance/OpenESPI-DataCustodian-java)

## OpenESPI-Common
This repository contains run-time and test code (no longer used) to support the original energy usage database and atom domains, repositories, and service methods.  Only the Energy Usage database exist in this repository.

## OpenESPI-DataCustodian
This repository contains run-time and test code (no longer used) to support API logic to extract data from the energy usage database.

This repository will be shared by two UTD Project teams acting jointly to provide the capabilities of the above two repositories.

## UTDsolv SP2021 ITSS 4395 Project
During the Spring 2021 The UTD Solv ITSS 4395 team was asked to review the current database design of the original OpenESPI-Common repository and recommend desing improvements for the energy usage database and define the retail customer database.  The deliverables from that project are contained in the "scripts" directory.

## UTD FA2022 CS4885 DataBase Project
The purpose of this project is to use the deliverables from the UTDsolv SP2021 ITSS 4395 Project to define the Energy Usage and Retail Customer databases.

## UTD FA2022 CS4885 API Project
The purpose of this project is to implement the documented NAESB REQ.21 ESPI ver 3.3 APIs to retrieve and transmit data from the Energy Usage and Retail Customer databases created by the UTD FA2022 CS4885 API Project.

## Project Resources
- Copies of the NAESB REQ.21 ESPI ver. 3.3 standard can be obtained by sending email to Elizabeth Mallett, Deputy Director, North American Energy Standards Board. Elizabeth Mallett <emallett@naesb.org>.
- Copies of the NAESB REQ.21 ESPI ver. 3.3 Energy Usage and Retail Customer schemas can be found in the "schemas" directory. The NAESB copyright prevents the use of the copyrighted schema file names.  Therefore, the "usage.xsd" schema contains the NAESB Energy Usage (i.e., espi.xsd) and the "retailcustomer.xsd" schema contains the NAESB Retail Customer (i.e., customer.xsd).
- The "atom.xsd" schema was developed under a NIST Grant and uses the Apache License 2.0 open source license.
- Slack channels for each project have been created for each project to enable teams to directly communicate with the project mentor.
- Weekly "Office Hours" will be held with the Project Mentor for each project team.  Each team is required to select the best day of the week and time for their weekly "office Hours".
- All meetings will be hosted using Zoom and recorded.

## Best Practices
- This repository may be forked or cloned.  It will be up to the teams to determine how best to complete their projects.
- All developed code must be tested using best-of-practice testing methodologies
- All Pull Request (PR) will be reviewed by the Project Mentor prior to inclusion in the project repository.
