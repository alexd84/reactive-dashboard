function getWsUri() {
	return "ws://" + window.location.host + "/live";
}

function createSocket() {
	socket = new WebSocket(getWsUri());

	socket.onopen = function() {
	};

	socket.onclose = function() {
		document.write("live stream is closed, please refresh page<br/>");
	};

	socket.onerror = function(error) {
        document.write("error occured: " + error + "<br/>");
    }

	socket.onmessage = onSocketMes;
}

function onSocketMes(event) {
	document.write(event.data + "<br/>");
}
