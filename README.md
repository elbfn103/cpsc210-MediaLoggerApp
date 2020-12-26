# CPSC 210 Personal Project Proposal

## Overview

I plan to design a personal media tracker program. More specifically, it is a program
where the user can record books, podcasts, movies, songs, etc that they've consumed. 

**Main functions:**
- Record names of media, author/creator
- Rating system for each entry
- Comment/notes/quotes system for each entry
- View information for each entry in a larger library

**Possible extra functions:**
- Pull information from the Internet on given media
- Periodically notify user with random 'inspiration' notes/quotes
- Generate data/statistics on media consumed
- Multiple different libraries (categorized)

## Reasoning

Personally, I would use an app like this, as I find myself losing track of what books
or articles I've read that really inspired me or sparked an idea. That's partly the
reason why I want to design a program like this, as I think it's a common problem for
people who consume a lot of media (especially in this digital age).

The idea of the program is to retain all the inspiration from various sources, and so
it would also be especially useful to people with creative hobbies/careers such as
artists or musicians. Even for programmers, it is very handy to note not only possible
project ideas, but also interesting articles on new technology or coding tips. 

## User Stories

Phase 1:
- As a user, I want to be able to add a media entry to my library.
- As a user, I want to be able to change the title and author of an entry in my library.
- As a user, I want to be able to add a rating to an entry in my library.
- As a user, I want to be able to delete a media entry from my library.

Phase 1 Extra:
- As a user, I want to be able to view a list of the media in my library.
- As a user, I want to be able to add comments to an entry in my library.

Phase 2:
- As a user, I want to be able to save my library to file.
- As a user, I want to be able to load my library from file.

## Phase 4: Task 2

I designed and tested the MediaList model to be robust. The removeEntry method within
MediaList throws a checked EntryNotFoundException if the given Entry is not found in
the list. The findEntry method throws the same EntryNotFoundException under similar
circumstances.

## Phase 4: Task 3

Refactor Main class:
- Far too long, includes everything for GUI
- Separate into different classes
- Buttons, listeners, sound, etc.
- Significantly improve readability, make future GUI changes much easier