fetch('http://localhost:8080/graphql', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify({
    query: `
      query {
        getAllBooksPaginated(offset: 2, limit: 2) {
                title
                author
                publishedYear
        }
      }
    `,
  }),
})
  .then(response => response.json())
  .then(data => console.log(data.data.getAllBooksPaginated));