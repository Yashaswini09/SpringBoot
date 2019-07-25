package com.cg.ticketcounter.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.client.ClientBuilder;

import org.biacode.jcronofy.api.client.CronofyClient;
import org.biacode.jcronofy.api.client.impl.CronofyClientImpl;
import org.biacode.jcronofy.api.model.common.CronofyResponse;
import org.biacode.jcronofy.api.model.request.ListCalendarsRequest;
import org.biacode.jcronofy.api.model.request.ReadEventsRequest;
import org.biacode.jcronofy.api.model.response.ListCalendarsResponse;
import org.biacode.jcronofy.api.model.response.ReadEventsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.cg.ticketcounter.exception.TicketCounterException;
import com.cg.ticketcounter.model.Bookings;
import com.cg.ticketcounter.model.Movie;
import com.cg.ticketcounter.model.Shows;
import com.cg.ticketcounter.model.User;
import com.cg.ticketcounter.service.BookingService;
import com.cg.ticketcounter.service.MovieService;
import com.cg.ticketcounter.service.SecurityService;
import com.cg.ticketcounter.service.ShowsService;
import com.cg.ticketcounter.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import ch.qos.logback.core.net.SyslogOutputStream;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ShowsService showsService;

	@Autowired
	private MovieService movieService;

	@Autowired
	private SecurityService securityService;
	@Autowired
	private BookingService bookingService;

	static int count = 0;

	String cronofyCode;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {

		model.addAttribute("userForm", new User());

		return "registration";
	}

	// @Secured(value = "ADMIN")
	@RequestMapping(value = { "adminPage" }, method = RequestMethod.GET)
	public String adminPage(Model model) {
		model.addAttribute("movieForm", new Movie());
		return "adminPage";
	}

	// @PreAuthorize("hasRole('USER')")
	// @Secured(value = "USER")
	@RequestMapping(value = { "userPage" }, method = RequestMethod.GET)
	public String userPage(Model model) {
		model.addAttribute("movieForm", new Movie());
		return "userPage";
	}

	// @PreAuthorize("hasRole('ADMIN')")
	// @Secured(value = "ADMIN")
	@RequestMapping(value = "/adminPage/addMoviePage")
	public String addMoviePage(Model model) {
		model.addAttribute("movieForm", new Movie());

		return "addMovie";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@Valid @ModelAttribute("userForm") User userForm, BindingResult bindingResult,
			Model model) throws TicketCounterException {

		if (userService.findByUsername(userForm.getUsername()) != null) {
			model.addAttribute("errorMessage", "UserName exists");
			return "registration";
		}

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		userForm.setRole("USER");
		userService.save(userForm);
		securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

		return "redirect:/userPage";
	}

	// @PreAuthorize("hasRole('ADMIN')")
	// @Secured(value = "ADMIN")
	@RequestMapping(value = "/adminPage/addMovie", method = RequestMethod.POST)
	public String addMovie(@Valid @ModelAttribute("movieForm") Movie movieForm, BindingResult bindingResult,
			Model model) throws TicketCounterException {

		if (bindingResult.hasErrors()) {
			return "addMovie";
		}

		movieService.save(movieForm);

		model.addAttribute("movieForm", movieForm);

		return "redirect:/adminPage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) throws Exception {

		if (count == 0) {

			String input = "02/12/2018";
			/*
			 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			 * LocalDate date = LocalDate.parse(input, formatter); System.out.printf("%s%n",
			 * date);
			 */

			userService.save(
					new User((long) 123, "vignesh", "hellohello", "hellohello", "USER", "9032849214", "vj@gmail.com"));
			userService.save(new User((long) 124, "admin", "hellohello", "hellohello", "ADMIN", "7896541230",
					"admin@gmail.com"));
			movieService.save(new Movie((long) 15689, "Hello", 5, 300));
			Shows show = new Shows(input, 4, 300, "06:00 PM-09:00 PM", movieService.findByName((long) 1));
			showsService.setShows(show);

			movieService.save(new Movie((long) 15489, "MCA", 6, 350));
			show = new Shows(input, 5, 600, "11:00 AM-02:00 PM", movieService.findByName((long) 2));
			showsService.setShows(show);

			movieService.save(new Movie((long) 15589, "Velaikkaran", 3, 500));
			show = new Shows(input, 6, 521, "03:00PM -05:00PM", movieService.findByName((long) 3));
			show = showsService.setShows(show);

			count++;
		}

		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	// @PreAuthorize("hasRole('ADMIN')")
	// @Secured(value = "ADMIN")
	@RequestMapping(value = { "/adminPage/movieListPage" }, method = RequestMethod.GET)
	public String movieListPage(Model model) throws TicketCounterException {

		List<Movie> movies = new ArrayList<Movie>();

		movies = movieService.getAllMovies();

		if (movies.isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}
		model.addAttribute("movies", movies);
		return "movieList";
	}

	// @PreAuthorize("hasRole('USER')")
	@RequestMapping(value = { "userPage/bookTicketPage" }, method = RequestMethod.GET)
	public String bookTicketPage(Model model) throws TicketCounterException {
		List<Movie> movies = null;

		movies = movieService.getAllMovies();

		if (movies.isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}
		model.addAttribute("movies", movies);
		return "bookTicket";
	}

	// @PreAuthorize("hasRole('ADMIN')")

	// @Secured(value = "ADMIN")
	@RequestMapping(value = { "adminPage/delete/{id}" }, method = RequestMethod.GET)
	public String deleteMovie(Model model, @PathVariable("id") Long id) throws TicketCounterException {

		Boolean check = true;

		check = movieService.deleteMovie(id);

		if (check == false)
			model.addAttribute("errorMessage", "Deletion Failed");
		List<Movie> movies = new ArrayList<Movie>();

		movies = movieService.getAllMovies();
		if (movies.isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("movies", movies);

		return "movieList";
	}

	// @PreAuthorize("hasRole('ADMIN')")
	// @Secured(value = "ADMIN")
	@RequestMapping(value = { "adminPage/showBookings/{show}" }, method = RequestMethod.GET)
	public String movieBookings(Model model, @PathVariable("show") long showId) throws TicketCounterException {

		Shows show = showsService.getShows(showId);
		if (show == null) {
			model.addAttribute("errorMessage", "No Such Show exists");
			return "error";
		}
		System.out.println("HEllo.......");
		if (show.getBookings().isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("show", show);

		return "showBookings";
	}

	@RequestMapping(value = { "adminPage/addShowsPage/{movie}" }, method = RequestMethod.GET)
	public String addShowsPage(Model model, @PathVariable("movie") Long movieId) throws TicketCounterException {

		Shows showForm = new Shows();
		showForm.setMovie(movieService.findByName(movieId));
		model.addAttribute("showForm", showForm);

		return "addShowsPage";
	}

	@RequestMapping(value = { "adminPage/addShow" }, method = RequestMethod.POST)
	public String addShows(Model model, @Valid @ModelAttribute("showForm") Shows show, BindingResult result)
			throws TicketCounterException {

		if (result.hasErrors()) {
			return "addShowsPage";
		}

		show = showsService.setShows(show);
		if (show == null) {
			model.addAttribute("errorMessage", "Adding of Show failed");
			return "error";
		}
		System.out.println("HEllo.......");
		if (show.getMovie() == null) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("movie", show.getMovie());

		return "adminDisplayShows";
	}

	@RequestMapping(value = { "adminPage/adminDisplayShows/{movieId}" }, method = RequestMethod.GET)
	public String adminDisplayShows(Model model, @PathVariable long movieId) throws TicketCounterException {

		Movie movie = movieService.findByName(movieId);

		if (movie.getShows().isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("movie", movie);
		return "adminDisplayShows";
	}

	@RequestMapping(value = { "displayShows/{movie}" }, method = RequestMethod.GET)
	public String displayShows(Model model, @PathVariable("movie") Long movieId) throws TicketCounterException {

		Movie movie = movieService.findByName(movieId);
		if (movie == null) {
			model.addAttribute("errorMessage", "No Such movie exists");
			return "error";
		}
		System.out.println("HEllo.......");

		if (movie.getShows().isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			System.out.println(movie.getShows());
			model.addAttribute("listNull", true);
		}

		model.addAttribute("movie", movie);

		return "movieShows";
	}

	/*
	 * @RequestMapping(value = { "searchMovie" }) public String searchMovie(Model
	 * model, @RequestParam("id") Long id) throws TicketCounterException { Movie
	 * movie = null;
	 * 
	 * movie = movieService.findByName(id);
	 * 
	 * if (movie == null) { model.addAttribute("listNull", false); } else {
	 * model.addAttribute("listNull", true); } model.addAttribute("movie", movie);
	 * return "movieList"; }
	 */

	// @PreAuthorize("hasRole('USER')")
	// @Secured(value = "USER")
	@RequestMapping(value = { "userPage/profile/{username}" })
	public String profile(Model model, @PathVariable("username") String username) throws TicketCounterException {
		User user = null;

		user = userService.findByUsername(username);

		if (user.getBookings().isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("user", user);
		return "profile";
	}

	@RequestMapping(value = { "userPage/cancelTicket/{username}/{bookingId}" })
	public String cancelTicket(Model model, @PathVariable("bookingId") Long bookingId,
			@PathVariable("username") String username) throws TicketCounterException {
		User user = null;

		bookingService.deleteBookings(bookingId);
		System.err.println("SUernameeeeeeeeeeeeee" + username);
		user = userService.findByUsername(username);
		if (user == null) {
			throw new TicketCounterException(1, "No Data Found");
		}

		if (user.getBookings().isEmpty()) {
			model.addAttribute("listNull", false);
		} else {
			model.addAttribute("listNull", true);
		}

		model.addAttribute("user", user);
		return "profile";
	}

	// @PreAuthorize("hasRole('USER')")
	// @Secured(value = "USER")
	@RequestMapping(value = { "/userPage/confirmTicket" }, method = RequestMethod.GET)
	public String confirmTicket(Model model, @RequestParam("showId") long showId, @RequestParam("userId") String name,
			@RequestParam("movieId") Long movieId, @RequestParam("ticketCount") int ticketCount)
			throws TicketCounterException {

		Bookings booking = null;
		int check = 0;

		Shows show = showsService.getShows(showId);
		booking = bookingService.save(new Bookings(userService.findByUsername(name), show, ticketCount));

		ticketCount = show.getNumberOfTickets() - ticketCount;

		check = showsService.updateNumberOfTickets(showId, ticketCount);

		if (check == 0) {
			return "confirmTicket";
		}
		model.addAttribute("bookingID", booking.getBookingId());
		model.addAttribute("message", "Tickets has been booked. Booking ID:" + booking.getBookingId());
		return "userPage";
	}

	@RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
	public String welcome(Model model) {

		model.addAttribute("movieForm", new Movie());
		return "adminLoginPage";
	}

	@RequestMapping(value = { "/error" }, method = RequestMethod.GET)
	public String error(Model model, @RequestParam("type") int type, @RequestParam("status") String status) {
		System.out.println("in error method");
		model.addAttribute("type", type);
		model.addAttribute("status", status);
		return "error";
	}

	public static String getEvents() {
		// Construct cronofy java client
		final CronofyClient cronofyClient = new CronofyClientImpl(
				ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build());
		// List calendars
		final CronofyResponse<ListCalendarsResponse> calendarsResult = cronofyClient
				.listCalendars(new ListCalendarsRequest("YyrsH1mZtQQQ5kGHpvItD1758_MH6-2f"));
		System.out.println(calendarsResult.getResponse().toString());
		// Read events
		final CronofyResponse<ReadEventsResponse> eventsResult = cronofyClient
				.readEvents(new ReadEventsRequest("YyrsH1mZtQQQ5kGHpvItD1758_MH6-2f", "Etc/UTC"));
		// If an error occur
		if (eventsResult.hasError()) {
			System.out.println(eventsResult.getError().toString());
		} else {
			System.out.println(eventsResult.getResponse().toString());
		}
		return eventsResult.getResponse().toString();
	}

	@RequestMapping(value = "/cronofyCode")
	public String cronofy(Model model, @RequestParam("code") String code, @RequestParam("state") String state) {
		// System.out.println("in cronofy method");
		// model.addAttribute("events", getEvents());
		cronofyCode = code;
		String payload = "{ \"client_id\":\"SFmhRHpnaQax2ASVnfKolDzTR8y6uAyH\",\"client_secret\":\"WXZZSEqDLwaH7AKWi6JSxmPyQi5GyQb1vvmm9TuXp7WR0tORYxQAr877045xriSkjosJxb3egqA_Vjhn5m84CQ\",\"grant_type\":\"authorization_code\",\"code\":\""
				+ cronofyCode + "\",\"redirect_uri\": \"http://localhost:8080/accessToken\"}";
		String requestUrl = "https://api.cronofy.com/oauth/token";
		sendPostRequest(requestUrl, payload);

		return "cronofyCode";
	}

	@RequestMapping(value = "/accessToken")
	public String accessToken(Model model, @RequestParam("code") String code) {
		// System.out.println("in cronofy method");
		// model.addAttribute("events", getEvents());
		// cronofyCode = code;

		String payload = "{ \"client_id\":\"SFmhRHpnaQax2ASVnfKolDzTR8y6uAyH\",\"client_secret\":\"WXZZSEqDLwaH7AKWi6JSxmPyQi5GyQb1vvmm9TuXp7WR0tORYxQAr877045xriSkjosJxb3egqA_Vjhn5m84CQ\",\"grant_type\":\"authorization_code\",\"code\":\""
				+ cronofyCode + "\",\"redirect_uri\": \"http://localhost:8080/accessToken\"}";
		String requestUrl = "https://api.cronofy.com/oauth/token";
		sendPostRequest(requestUrl, payload);

		return "accessToken";
	}

	public static String sendPostRequest(String requestUrl, String payload) {
		StringBuffer jsonString = new StringBuffer();
		try {
			URL url = new URL(requestUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			writer.write(payload);
			writer.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			String line;
			while ((line = br.readLine()) != null) {
				jsonString.append(line);
			}
			br.close();
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return jsonString.toString();
	}

	@RequestMapping(value = "/getCalender")
	void getCalender(final String[] args) {
		// Construct cronofy java client
		final CronofyClient cronofyClient = new CronofyClientImpl(
				ClientBuilder.newBuilder().register(JacksonJsonProvider.class).build());
		// List calendars
		System.out.println(" List calendars");
		final CronofyResponse<ListCalendarsResponse> calendarsResult = cronofyClient
				.listCalendars(new ListCalendarsRequest("YyrsH1mZtQQQ5kGHpvItD1758_MH6-2f"));
		System.out.println(calendarsResult.getResponse().toString());
		// Read events
		System.out.println("  Read events");

		final CronofyResponse<ReadEventsResponse> eventsResult = cronofyClient
				.readEvents(new ReadEventsRequest("YyrsH1mZtQQQ5kGHpvItD1758_MH6-2f", "Etc/UTC"));
		// If an error occur
		System.out.println("  If an error occur");

		if (eventsResult.hasError()) {
			System.out.println(eventsResult.getError().toString());
		} else {
			System.out.println(eventsResult.getResponse().toString());
		}
	}
	
	@RequestMapping(value = "/getCalenderList")
	void getCalenderList(final String[] args) {
		String accessToken="YyrsH1mZtQQQ5kGHpvItD1758_MH6-2f";
		

		// RestTemplate to make call to Cronofy Real time scheduling
		ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer "+accessToken);

		HttpEntity<String> request = new HttpEntity<String>("requestJson",headers);

		String fooResourceUrl = "https://api.cronofy.com/v1/calendars";

		JsonNode url = restTemplate.postForObject(fooResourceUrl, request, JsonNode.class);
		
		System.out.println("-------------------------"+url.toString());

	}
	

	private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 5000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		return clientHttpRequestFactory;
	}
	
	
	
}
