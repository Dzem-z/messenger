export default function fetchData(uri) {
    return new Promise((resolve, reject) => {
        fetch(uri)
            .then(response => {
                const reader = response.body.getReader();
                const decoder = new TextDecoder('utf-8');
                let data = '';
  
                // Recursive function to read the stream
                function read() {
                    reader.read()
                        .then(({ done, value }) => {
                            if (done) {
                                console.log(data);
                                resolve(JSON.parse(data)); // Resolve the promise with the final data
                                return;
                            }
                            data += decoder.decode(value, { stream: true });
                            read(); // Continue reading
                        })
                        .catch(err => reject(err)); // Reject the promise if there's an error
                }
  
                read(); // Start reading the stream
            })
            .catch(err => reject(err)); // Reject the promise if fetch fails
    });
  }