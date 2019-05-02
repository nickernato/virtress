# virtress
Virtress is a virtual server that takes in requests and matches criteria given to return the matching response.
# Features
- Creates groups of matches to respond with the specified response if a group matches.
- Different matcher types for matchers in group include"
  - URL Matching
  - HTTP Method Matching
  - Request Body Matching
  - Match Request Headers
- Return response from matching group
- Return content type from matching group
- Return response code from matching group
- Full list of response codes for response
- Configurable port through program arguments or configuration file
# How to use
- Download the program
- Configure the port through program arguments or through the server config located in /com/virtress/server/config/virtress_config.json.
  - Configuring the port through program arguments will override the port configured in virtress_config.json.
  - The default port if no configuration is specified is 2800.
