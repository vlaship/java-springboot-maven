/**
 * Application configuration
 */

const config = {
  // API base URL
  apiBaseUrl: 'http://localhost:19999/book-store-facade-service/v1',
  
  // API endpoints
  endpoints: {
    auth: {
      login: '/auth/login',
    },
    author: {
      base: '/author',
      byId: (id) => `/author/${id}`,
    },
    book: {
      base: '/book',
      byId: (id) => `/book/${id}`,
      byAuthor: (authorId) => `/book/author/${authorId}`,
    },
    store: {
      base: '/store',
      byId: (id) => `/store/${id}`,
    },
  },
  
  // Form validation rules
  validation: {
    author: {
      name: {
        min: 2,
        max: 50,
      },
    },
    store: {
      name: {
        min: 2,
        max: 100,
      },
    },
  },
};

export default config;