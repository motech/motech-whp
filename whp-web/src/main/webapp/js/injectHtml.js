function set_html( id, html ) {
    // create orphan element set HTML to
    var orphNode = document.createElement('div');
    orphNode.innerHTML = html;

    // get the script nodes, add them into an arrary, and remove them from orphan node
    var scriptNodes = orphNode.getElementsByTagName('script');
    var scripts = [];
    while(scriptNodes.length) {
        // push into script array
        var node = scriptNodes[0];
        scripts.push(node.text);

        // then remove it
        node.parentNode.removeChild(node);
    }

    // add html to place holder element (note: we are adding the html before we execute the scripts)
    document.getElementById(id).innerHTML = orphNode.innerHTML;

    // execute stored scripts
    var head = document.getElementsByTagName('head')[0];
    while(scripts.length) {
        // create script node
        var scriptNode = document.createElement('script');
        scriptNode.type = 'text/javascript';
        scriptNode.text = scripts.shift(); // add the code to the script node
        head.appendChild(scriptNode); // add it to the page
        head.removeChild(scriptNode); // then remove it
    }
}