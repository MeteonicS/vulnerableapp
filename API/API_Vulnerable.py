from flask import Flask, request, render_template_string
import re, os, json, xml.etree.ElementTree as ET
from ldap3 import Server, Connection, ALL

app = Flask(__name__)

# Buffer Overflow
@app.route('/buffer', methods=['POST'])
def buffer_overflow():
    data = request.form['data']
    buffer = 'A' * 1024  # Vulnerable buffer
    return buffer + data

# Command Injection
@app.route('/cmd', methods=['POST'])
def command_injection():
    cmd = request.form['cmd']
    os.system(cmd)  # Vulnerable to command injection
    return "Command executed"

# CORS Wildcard Vulnerability
@app.after_request
def add_cors_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'  # Vulnerable CORS configuration
    return response

# Cross-Site Scripting (XSS)
@app.route('/xss', methods=['POST'])
def xss_vulnerability():
    user_input = request.form['input']
    return render_template_string(f"<h1>{user_input}</h1>")  # Vulnerable to XSS

# Integer Overflow
@app.route('/int-overflow', methods=['POST'])
def integer_overflow():
    num = int(request.form['number'])
    result = num + 2147483647  # Vulnerable to integer overflow
    return str(result)

# JSON Depth Overflow
@app.route('/json-depth', methods=['POST'])
def json_depth_overflow():
    data = json.loads(request.data)
    return json.dumps(data)  # Vulnerable to JSON depth overflow

# LDAP Injection
@app.route('/ldap', methods=['POST'])
def ldap_injection():
    server = Server('ldap://localhost', get_info=ALL)
    conn = Connection(server)
    search_filter = f"(uid={request.form['user']})"
    conn.search('dc=example,dc=com', search_filter)  # Vulnerable to LDAP injection
    return str(conn.entries)

# Regex DoS
@app.route('/regex', methods=['POST'])
def regex_dos():
    pattern = request.form['pattern']
    re.compile(pattern)  # Vulnerable to ReDoS
    return "Regex compiled"

# SQL Injection
@app.route('/sql', methods=['POST'])
def sql_injection():
    query = request.form['query']
    result = f"SELECT * FROM users WHERE name = '{query}'"  # Vulnerable to SQL injection
    return result

# String Validation Vulnerability
@app.route('/string-validate', methods=['POST'])
def string_validation():
    data = request.form['data']
    if "<script>" in data:  # Very weak validation
        return "Unsafe string detected"
    return data

# XML External Entity (XXE) Vulnerability
@app.route('/xml', methods=['POST'])
def xml_external_entity():
    xml_data = request.data
    try:
        # Enable external entity resolution
        parser = ET.XMLParser(resolve_entities=True)
        tree = ET.fromstring(xml_data, parser=parser)
        return ET.tostring(tree).decode()
    except ET.ParseError as e:
        return f"XML Parsing Error: {e}"

# Cross-Site Tracing (XST)
@app.route('/trace', methods=['TRACE'])
def cross_site_tracing():
    return request.data  # Vulnerable to XST

# Non-HTTPS Links in Response
@app.route('/non-https', methods=['GET'])
def non_https_links():
    return "<a href='http://example.com'>Insecure Link</a>"  # Non-HTTPS link in response body

# General Server Vulnerabilities (Placeholder)
@app.route('/general-server', methods=['GET'])
def general_server_vulnerabilities():
    return "General server vulnerabilities simulated"

# Heartbleed Vulnerability (Placeholder)
@app.route('/heartbleed', methods=['GET'])
def heartbleed():
    return "Heartbleed vulnerability simulated"

# HTTP Host Header Injection
@app.route('/host-header', methods=['GET'])
def host_header_injection():
    host = request.headers.get('Host')
    return f"Host header is {host}"  # Vulnerable to Host Header Injection

# OpenSSL CCS Injection Vulnerability (Placeholder)
@app.route('/openssl-ccs', methods=['GET'])
def openssl_ccs():
    return "OpenSSL CCS Injection vulnerability simulated"

# SSL/TLS Renegotiation Vulnerability (Placeholder)
@app.route('/tls-renegotiation', methods=['GET'])
def tls_renegotiation():
    return "TLS Renegotiation vulnerability simulated"

# TLS Protocol Downgrade Attack (Placeholder)
@app.route('/tls-downgrade', methods=['GET'])
def tls_downgrade():
    return "TLS Protocol Downgrade Attack simulated"

# TLS ROBOT Attack (Placeholder)
@app.route('/tls-robot', methods=['GET'])
def tls_robot():
    return "TLS ROBOT Attack simulated"

# TLS/SSL CRIME Attack (Placeholder)
@app.route('/tls-crime', methods=['GET'])
def tls_crime():
    return "TLS/SSL CRIME Attack simulated"

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)  # Run without SSL context for HTTP
