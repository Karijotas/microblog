import './App.css';
import { Home } from './Pages/Home';
import 'bootstrap/dist/css/bootstrap.min.css';
import { HashRouter, Route, Routes } from 'react-router-dom';
import { Register } from './Pages/Register';
import { Post } from './Pages/Post';
import { Counter } from './Pages/Counter';
import { Update } from './Pages/Update';


function App() {
  return (
    <div>
      <Counter/>
      <HashRouter >
        <Routes>
          <Route path='/' element={<Home />} />
          <Route path='/register' element={<Register />} />
          <Route path='/post' element={<Post />} />
          <Route path='/update/:id' element={<Update/>} />
          

        </Routes>
      </HashRouter>
    </div>

  );
}

export default App;
