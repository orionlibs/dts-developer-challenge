import { Application } from 'express';
import axios, { isAxiosError } from 'axios';


function createApiErrorMessage(error: unknown): string {
  if (isAxiosError(error) && error.response) {
    // API responded with an error status (4xx or 5xx)
    const status = error.response.status;
    const message = error.response.data?.message || 'No additional error message provided.';
    return `Error: ${status}. ${message}`;
  } else if (isAxiosError(error) && error.request) {
    return 'Could not connect to the API service. Please try again later.';
  } else {
    return 'An unexpected error occurred.';
  }
}


export default function (app: Application): void {
  app.get('/', async (req, res) => {
    try {
      const response = await axios.get('http://localhost:4000/v1/cases');
      console.log(response.data);
      res.render('home', { "cases": response.data.cases });
    } catch (error) {
      const errorMessage = createApiErrorMessage(error);
      res.render('home', { cases: [], error: errorMessage });
    }
  });


  // POST /cases — create a new case
  app.post('/cases', async (req, res) => {
    // extract fields from the request body
    const { case_number, title, description, due_date_time } = req.body;
    const payload = {
      case_number,
      title,
      description,
      due_date_time
    };

    try {
      await axios.post(
        'http://localhost:4000/v1/cases',
        payload,
        { headers: { 'Content-Type': 'application/json' } }
      );

      // on success, redirect back to the list
      res.redirect('/');
    } catch (error) {
      const errorMessage = createApiErrorMessage(error);
      res.render('home', { cases: [], error: errorMessage });
    }
  });


  app.delete('/cases/:id', async (req, res) => {
    const caseId = req.params.id;

    try {
      await axios.delete(`http://localhost:4000/v1/cases/${caseId}`);
      res.status(200).json({ message: 'Case deleted successfully' });
    } catch (error) {
      const errorMessage = createApiErrorMessage(error);
      res.render('home', { cases: [], error: errorMessage });
    }
  });


app.patch('/cases/statuses', async (req, res) => {
    // extract fields from the request body
    console.log("running patch");
    const { status, caseIDToChange } = req.body;
    const payload = {
      status
    };

    try {
      await axios.patch(
        `http://localhost:4000/v1/cases/${caseIDToChange}/statuses`,
        payload,
        { headers: { 'Content-Type': 'application/json' } }
      );

      // on success, redirect back to the list
      res.redirect('/');
    } catch (error) {
      const errorMessage = createApiErrorMessage(error);
      res.render('home', { cases: [], error: errorMessage });
    }
  });
}
