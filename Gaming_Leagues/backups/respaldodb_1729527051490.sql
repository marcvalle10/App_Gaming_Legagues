--
-- PostgreSQL database dump
--

-- Dumped from database version 16.1
-- Dumped by pg_dump version 16.1

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: games; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.games (
    game_code integer NOT NULL,
    game_name character varying(100) NOT NULL,
    game_description text
);


ALTER TABLE public.games OWNER TO developer;

--
-- Name: games_game_code_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.games_game_code_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.games_game_code_seq OWNER TO developer;

--
-- Name: games_game_code_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.games_game_code_seq OWNED BY public.games.game_code;


--
-- Name: leagues; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.leagues (
    league_id integer NOT NULL,
    league_name character varying(100) NOT NULL,
    league_details text,
    start_date date,
    end_date date
);


ALTER TABLE public.leagues OWNER TO developer;

--
-- Name: leagues_games; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.leagues_games (
    league_id integer NOT NULL,
    game_code integer NOT NULL
);


ALTER TABLE public.leagues_games OWNER TO developer;

--
-- Name: leagues_league_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.leagues_league_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.leagues_league_id_seq OWNER TO developer;

--
-- Name: leagues_league_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.leagues_league_id_seq OWNED BY public.leagues.league_id;


--
-- Name: leagues_teams; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.leagues_teams (
    league_id integer NOT NULL,
    team_id integer NOT NULL
);


ALTER TABLE public.leagues_teams OWNER TO developer;

--
-- Name: matches; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.matches (
    match_id integer NOT NULL,
    game_code integer NOT NULL,
    player_1_id integer NOT NULL,
    player_2_id integer NOT NULL,
    match_date date DEFAULT CURRENT_DATE,
    result character varying(10),
    CONSTRAINT matches_result_check CHECK (((result)::text = ANY ((ARRAY['win'::character varying, 'lose'::character varying, 'draw'::character varying])::text[])))
);


ALTER TABLE public.matches OWNER TO developer;

--
-- Name: matches_match_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.matches_match_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.matches_match_id_seq OWNER TO developer;

--
-- Name: matches_match_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.matches_match_id_seq OWNED BY public.matches.match_id;


--
-- Name: matches_teams; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.matches_teams (
    match_id integer NOT NULL,
    league_id integer,
    team1_id integer,
    team2_id integer,
    match_date date,
    game_code integer,
    result character varying(10),
    CONSTRAINT matches_teams_result_check CHECK (((result)::text = ANY ((ARRAY['win'::character varying, 'lose'::character varying, 'draw'::character varying])::text[])))
);


ALTER TABLE public.matches_teams OWNER TO developer;

--
-- Name: matches_teams_match_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.matches_teams_match_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.matches_teams_match_id_seq OWNER TO developer;

--
-- Name: matches_teams_match_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.matches_teams_match_id_seq OWNED BY public.matches_teams.match_id;


--
-- Name: players; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.players (
    player_id integer NOT NULL,
    first_name character varying(100) NOT NULL,
    last_name character varying(100) NOT NULL,
    gender character(1),
    address character varying(255),
    CONSTRAINT players_gender_check CHECK ((gender = ANY (ARRAY['M'::bpchar, 'F'::bpchar, 'O'::bpchar])))
);


ALTER TABLE public.players OWNER TO developer;

--
-- Name: players_game_ranking; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.players_game_ranking (
    player_id integer NOT NULL,
    game_code integer NOT NULL,
    ranking integer,
    CONSTRAINT players_game_ranking_ranking_check CHECK ((ranking > 0))
);


ALTER TABLE public.players_game_ranking OWNER TO developer;

--
-- Name: players_player_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.players_player_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.players_player_id_seq OWNER TO developer;

--
-- Name: players_player_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.players_player_id_seq OWNED BY public.players.player_id;


--
-- Name: players_teams; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.players_teams (
    player_id integer NOT NULL,
    team_id integer NOT NULL,
    date_assigned date DEFAULT CURRENT_DATE
);


ALTER TABLE public.players_teams OWNER TO developer;

--
-- Name: team_players; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.team_players (
    team_id integer NOT NULL,
    player_id integer NOT NULL,
    date_from date NOT NULL,
    date_to date
);


ALTER TABLE public.team_players OWNER TO developer;

--
-- Name: teams; Type: TABLE; Schema: public; Owner: developer
--

CREATE TABLE public.teams (
    team_id integer NOT NULL,
    created_by_player_id integer NOT NULL,
    team_name character varying(100) NOT NULL,
    date_created date DEFAULT CURRENT_DATE,
    date_disbanded date
);


ALTER TABLE public.teams OWNER TO developer;

--
-- Name: teams_team_id_seq; Type: SEQUENCE; Schema: public; Owner: developer
--

CREATE SEQUENCE public.teams_team_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.teams_team_id_seq OWNER TO developer;

--
-- Name: teams_team_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: developer
--

ALTER SEQUENCE public.teams_team_id_seq OWNED BY public.teams.team_id;


--
-- Name: games game_code; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.games ALTER COLUMN game_code SET DEFAULT nextval('public.games_game_code_seq'::regclass);


--
-- Name: leagues league_id; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues ALTER COLUMN league_id SET DEFAULT nextval('public.leagues_league_id_seq'::regclass);


--
-- Name: matches match_id; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches ALTER COLUMN match_id SET DEFAULT nextval('public.matches_match_id_seq'::regclass);


--
-- Name: matches_teams match_id; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams ALTER COLUMN match_id SET DEFAULT nextval('public.matches_teams_match_id_seq'::regclass);


--
-- Name: players player_id; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players ALTER COLUMN player_id SET DEFAULT nextval('public.players_player_id_seq'::regclass);


--
-- Name: teams team_id; Type: DEFAULT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams ALTER COLUMN team_id SET DEFAULT nextval('public.teams_team_id_seq'::regclass);


--
-- Data for Name: games; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.games (game_code, game_name, game_description) FROM stdin;
1	Chess	Strategy board game
2	Soccer	Team sport played with a spherical ball
3	League of Legends	Multiplayer online battle arena video game
4	FIFA	Soccer simulation video game
5	Overwatch	Team-based multiplayer first-person shooter game
\.


--
-- Data for Name: leagues; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.leagues (league_id, league_name, league_details, start_date, end_date) FROM stdin;
1	Junior League	For players under 20 years old	\N	\N
2	Women's League	All female players	\N	\N
3	Pro League	For professional gamers	\N	\N
4	Amateur League	For amateur gamers	\N	\N
5	Mixed League	Open to gamers of all skill levels	\N	\N
\.


--
-- Data for Name: leagues_games; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.leagues_games (league_id, game_code) FROM stdin;
1	1
2	2
\.


--
-- Data for Name: leagues_teams; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.leagues_teams (league_id, team_id) FROM stdin;
1	1
2	4
3	5
1	2
\.


--
-- Data for Name: matches; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.matches (match_id, game_code, player_1_id, player_2_id, match_date, result) FROM stdin;
1	1	1	2	2024-05-13	win
2	2	2	1	2024-05-13	lose
3	1	1	2	2024-05-13	lose
4	2	2	3	2024-05-13	win
5	3	3	1	2024-05-13	draw
13	1	3	1	2024-05-14	\N
18	2	2	2	2024-09-10	\N
\.


--
-- Data for Name: matches_teams; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.matches_teams (match_id, league_id, team1_id, team2_id, match_date, game_code, result) FROM stdin;
1	1	1	2	2022-01-01	1	\N
2	2	2	3	2022-01-02	2	\N
3	1	1	3	2022-01-03	1	\N
4	2	1	2	2022-01-04	2	\N
5	1	2	3	2022-01-05	1	\N
10	1	1	2	2024-05-10	1	win
11	1	3	4	2024-05-11	1	lose
12	2	5	3	2024-05-12	2	draw
13	2	4	2	2024-05-13	2	win
14	\N	1	2	2024-05-13	2	\N
15	\N	2	5	2024-05-13	3	\N
16	\N	1	2	2024-05-14	5	\N
17	\N	2	5	2024-05-14	4	\N
\.


--
-- Data for Name: players; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.players (player_id, first_name, last_name, gender, address) FROM stdin;
1	John	Doe	M	1234 Elm St
2	Jane	Smith	F	5678 Maple Ave
3	Luis	Sanchez	M	1348 Arm Ct
8	Martin	Aragon	M	1684 Arm CT
13	Daniel	Salcido	M	123 COL
\.


--
-- Data for Name: players_game_ranking; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.players_game_ranking (player_id, game_code, ranking) FROM stdin;
1	1	1
2	2	2
3	1	3
1	2	1
2	3	2
\.


--
-- Data for Name: players_teams; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.players_teams (player_id, team_id, date_assigned) FROM stdin;
\.


--
-- Data for Name: team_players; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.team_players (team_id, player_id, date_from, date_to) FROM stdin;
1	1	2022-01-01	\N
2	2	2022-01-02	\N
3	3	2022-01-03	\N
1	3	2022-01-01	\N
2	1	2022-01-02	\N
1	1	2024-05-13	\N
4	3	2023-04-10	\N
3	2	2024-05-14	\N
\.


--
-- Data for Name: teams; Type: TABLE DATA; Schema: public; Owner: developer
--

COPY public.teams (team_id, created_by_player_id, team_name, date_created, date_disbanded) FROM stdin;
1	1	The Eagles	2024-05-02	\N
2	2	The Hawks	2024-05-02	\N
3	3	The Wolves	2024-05-13	\N
4	1	The Lions	2024-05-13	\N
5	2	The Tigers	2024-05-13	\N
\.


--
-- Name: games_game_code_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.games_game_code_seq', 6, true);


--
-- Name: leagues_league_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.leagues_league_id_seq', 5, true);


--
-- Name: matches_match_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.matches_match_id_seq', 18, true);


--
-- Name: matches_teams_match_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.matches_teams_match_id_seq', 18, true);


--
-- Name: players_player_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.players_player_id_seq', 13, true);


--
-- Name: teams_team_id_seq; Type: SEQUENCE SET; Schema: public; Owner: developer
--

SELECT pg_catalog.setval('public.teams_team_id_seq', 5, true);


--
-- Name: games games_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.games
    ADD CONSTRAINT games_pkey PRIMARY KEY (game_code);


--
-- Name: leagues_games leagues_games_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_games
    ADD CONSTRAINT leagues_games_pkey PRIMARY KEY (league_id, game_code);


--
-- Name: leagues leagues_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues
    ADD CONSTRAINT leagues_pkey PRIMARY KEY (league_id);


--
-- Name: leagues_teams leagues_teams_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_teams
    ADD CONSTRAINT leagues_teams_pkey PRIMARY KEY (league_id, team_id);


--
-- Name: matches matches_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_pkey PRIMARY KEY (match_id);


--
-- Name: matches_teams matches_teams_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams
    ADD CONSTRAINT matches_teams_pkey PRIMARY KEY (match_id);


--
-- Name: players_game_ranking players_game_ranking_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT players_game_ranking_pkey PRIMARY KEY (player_id, game_code);


--
-- Name: players players_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT players_pkey PRIMARY KEY (player_id);


--
-- Name: players_teams players_teams_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_teams
    ADD CONSTRAINT players_teams_pkey PRIMARY KEY (player_id, team_id);


--
-- Name: team_players team_players_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT team_players_pkey PRIMARY KEY (team_id, player_id, date_from);


--
-- Name: teams teams_pkey; Type: CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT teams_pkey PRIMARY KEY (team_id);


--
-- Name: leagues_games leagues_games_game_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_games
    ADD CONSTRAINT leagues_games_game_code_fkey FOREIGN KEY (game_code) REFERENCES public.games(game_code) ON DELETE CASCADE;


--
-- Name: leagues_games leagues_games_league_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_games
    ADD CONSTRAINT leagues_games_league_id_fkey FOREIGN KEY (league_id) REFERENCES public.leagues(league_id) ON DELETE CASCADE;


--
-- Name: leagues_teams leagues_teams_league_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_teams
    ADD CONSTRAINT leagues_teams_league_id_fkey FOREIGN KEY (league_id) REFERENCES public.leagues(league_id) ON DELETE CASCADE;


--
-- Name: leagues_teams leagues_teams_team_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.leagues_teams
    ADD CONSTRAINT leagues_teams_team_id_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) ON DELETE CASCADE;


--
-- Name: matches matches_game_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_game_code_fkey FOREIGN KEY (game_code) REFERENCES public.games(game_code);


--
-- Name: matches matches_player_1_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_player_1_id_fkey FOREIGN KEY (player_1_id) REFERENCES public.players(player_id) ON DELETE CASCADE;


--
-- Name: matches matches_player_2_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_player_2_id_fkey FOREIGN KEY (player_2_id) REFERENCES public.players(player_id) ON DELETE CASCADE;


--
-- Name: matches_teams matches_teams_game_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams
    ADD CONSTRAINT matches_teams_game_code_fkey FOREIGN KEY (game_code) REFERENCES public.games(game_code);


--
-- Name: matches_teams matches_teams_league_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams
    ADD CONSTRAINT matches_teams_league_id_fkey FOREIGN KEY (league_id) REFERENCES public.leagues(league_id);


--
-- Name: matches_teams matches_teams_team1_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams
    ADD CONSTRAINT matches_teams_team1_id_fkey FOREIGN KEY (team1_id) REFERENCES public.teams(team_id);


--
-- Name: matches_teams matches_teams_team2_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.matches_teams
    ADD CONSTRAINT matches_teams_team2_id_fkey FOREIGN KEY (team2_id) REFERENCES public.teams(team_id);


--
-- Name: players_game_ranking players_game_ranking_game_code_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT players_game_ranking_game_code_fkey FOREIGN KEY (game_code) REFERENCES public.games(game_code) ON DELETE CASCADE;


--
-- Name: players_game_ranking players_game_ranking_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_game_ranking
    ADD CONSTRAINT players_game_ranking_player_id_fkey FOREIGN KEY (player_id) REFERENCES public.players(player_id) ON DELETE CASCADE;


--
-- Name: players_teams players_teams_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_teams
    ADD CONSTRAINT players_teams_player_id_fkey FOREIGN KEY (player_id) REFERENCES public.players(player_id) ON DELETE CASCADE;


--
-- Name: players_teams players_teams_team_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.players_teams
    ADD CONSTRAINT players_teams_team_id_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) ON DELETE CASCADE;


--
-- Name: team_players team_players_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT team_players_player_id_fkey FOREIGN KEY (player_id) REFERENCES public.players(player_id) ON DELETE CASCADE;


--
-- Name: team_players team_players_team_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.team_players
    ADD CONSTRAINT team_players_team_id_fkey FOREIGN KEY (team_id) REFERENCES public.teams(team_id) ON DELETE CASCADE;


--
-- Name: teams teams_created_by_player_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: developer
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT teams_created_by_player_id_fkey FOREIGN KEY (created_by_player_id) REFERENCES public.players(player_id) ON DELETE SET NULL;


--
-- PostgreSQL database dump complete
--

