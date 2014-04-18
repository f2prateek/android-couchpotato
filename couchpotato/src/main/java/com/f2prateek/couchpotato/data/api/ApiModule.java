package com.f2prateek.couchpotato.data.api;

import com.f2prateek.couchpotato.data.api.moviedb.TMDbApiModule;
import dagger.Module;

@Module(
    includes = TMDbApiModule.class,
    complete = false,
    library = true)
public final class ApiModule {

}
