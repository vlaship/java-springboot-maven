# Book Store UI

A React-based user interface for managing books, authors, and stores.

## Features

- **Authentication**: Secure login system
- **Authors Management**: Add, edit, view, and delete authors
- **Books Management**: Add, edit, view, and delete books, with author and store associations
- **Stores Management**: Add, edit, view, and delete stores
- **Responsive Design**: Works on desktop and mobile devices

## Project Structure

The project follows a modular architecture for better maintainability:

```
src/
├── components/           # React components
│   ├── auth/             # Authentication components
│   ├── authors/          # Author management components
│   ├── books/            # Book management components
│   ├── common/           # Shared UI components
│   ├── home/             # Home page components
│   ├── layout/           # Layout components
│   └── stores/           # Store management components
├── services/             # API services
│   ├── api.js            # Base API utilities
│   ├── authService.js    # Authentication service
│   ├── authorService.js  # Author service
│   ├── bookService.js    # Book service
│   └── storeService.js   # Store service
├── config.js             # Application configuration
├── App.js                # Main application component
├── App.css               # Global styles
└── index.js              # Application entry point
```

## Code Organization

- **Components**: Each feature has its own folder with related components
- **Services**: API calls are centralized in service modules
- **Configuration**: API URLs and validation rules are centralized in config.js
- **Styling**: Mix of CSS classes and inline styles with Ant Design components

## Technologies Used

- **React**: UI library
- **React Router**: For navigation
- **Ant Design**: UI component library
- **Fetch API**: For making HTTP requests

## Getting Started

1. Install dependencies:
   ```
   npm install
   ```

2. Start the development server:
   ```
   npm start
   ```

3. Build for production:
   ```
   npm run build
   ```

## API Integration

The UI connects to a backend API at `http://localhost:19999/book-store-facade-service/v1`. The API endpoints are:

- **Authentication**: `/auth/login`
- **Authors**: `/author`
- **Books**: `/book`
- **Stores**: `/store`

## Best Practices Implemented

- **Component Separation**: Each component has a single responsibility
- **Service Layer**: API calls are abstracted into service modules
- **Error Handling**: Consistent error handling throughout the application
- **Loading States**: Proper loading indicators for async operations
- **Configuration**: Centralized configuration for easy maintenance
- **Documentation**: JSDoc comments for better code understanding
- **Consistent Styling**: Unified styling approach

## Future Improvements

- Add TypeScript for better type safety
- Implement a state management solution (Redux, Context API)
- Add unit and integration tests
- Improve accessibility
- Add internationalization support