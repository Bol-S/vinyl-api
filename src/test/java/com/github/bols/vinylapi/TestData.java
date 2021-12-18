package com.github.bols.vinylapi;

import com.github.bols.vinylapi.model.Album;
import com.github.bols.vinylapi.model.Artist;
import com.github.bols.vinylapi.model.MusicGroup;
import com.github.bols.vinylapi.model.enums.Condition;

import java.util.*;

public class TestData {

    public static class SampleArtist {

        public static Optional<Artist> getArtist01() {
            return Optional.of(new Artist(1, "Onyx", null));
        }

        public static Optional<Artist> getArtist02() {
            return Optional.of(new Artist(2, "Sticky Fingaz", "Kirk Jones"));
        }

        public static Optional<Artist> getArtist03() {
            return Optional.of(new Artist(3, "Fredro Starr", "Fred Lee Scrugs"));
        }

        public static Optional<Artist> getArtist04() {
            return Optional.of(new Artist(4, "Das Efx", null));
        }

        public static Optional<Artist> getArtist05() {
            return Optional.of(new Artist(5, "Krazy Drayz", "Andre Weston"));
        }

        public static Optional<Artist> getArtist06() {
            return Optional.of(new Artist(6, "Skoob", "William Hines"));
        }

        public static List<Artist> getAllArtists() {
            return Arrays.asList(getArtist01().orElseThrow(), getArtist02().orElseThrow(), getArtist03().orElseThrow(), getArtist04().orElseThrow(), getArtist05().orElseThrow(), getArtist06().orElseThrow());
        }
    }

    public static class SampleMusicGroup{

        public static Optional<MusicGroup> getMusicGroup01(){
            return Optional.of(new MusicGroup(1, "Onyx", Set.of(SampleArtist.getArtist02().orElseThrow(), SampleArtist.getArtist03().orElseThrow())));
        }

        public static Optional<MusicGroup> getMusicGroup02(){
            return Optional.of(new MusicGroup(2, "Das Efx", Set.of(SampleArtist.getArtist05().orElseThrow(), SampleArtist.getArtist06().orElseThrow())));
        }

        public static List<MusicGroup> getAllMusicGroups(){
            return Arrays.asList(getMusicGroup01().orElseThrow(), getMusicGroup02().orElseThrow());
        }
    }

    public static class SampleAlbum{

        public static Optional<Album> getAlbum01(){
            return Optional.of(new Album(1, "Bacdafucup", 1993, "JMJ", SampleArtist.getArtist01().orElseThrow(), Condition.NEAR_MINT));
        }

        public static Optional<Album> getAlbum02(){
            return Optional.of(new Album(2, "All We Got Iz Us", 1995, "JMJ", SampleArtist.getArtist01().orElseThrow(), Condition.VERY_GOOD_PLUS));
        }

        public static Optional<Album> getAlbum03(){
            return Optional.of(new Album(3, "Shut 'Em Down", 1998, "JMJ", SampleArtist.getArtist01().orElseThrow(), Condition.NEAR_MINT));
        }

        public static Optional<Album> getAlbum04(){
            return Optional.of(new Album(4, "Dead Serious", 1991, "EastWest Records America", SampleArtist.getArtist04().orElseThrow(), Condition.NEAR_MINT));
        }

        public static Optional<Album> getAlbum05(){
            return Optional.of(new Album(5, "Straight Up Sewaside", 1993, "EastWest Records America", SampleArtist.getArtist04().orElseThrow(), Condition.MINT));
        }

        public static Optional<Album> getAlbum06(){
            return Optional.of(new Album(6, "Decade... But Wait It Gets Worse", 1993, "Universal Records", SampleArtist.getArtist02().orElseThrow(), Condition.VERY_GOOD_PLUS));
        }

        public static Optional<Album> getAlbum07(){
            return Optional.of(new Album(7, "Firestarr", 2001, "Koch Records", SampleArtist.getArtist03().orElseThrow(), Condition.NEAR_MINT));
        }

        public static Optional<Album> getAlbum08(){
            return Optional.of(new Album(8, "Don't Get Mad Get Money", 2003, "D3 Entertainment", SampleArtist.getArtist03().orElseThrow(), Condition.GOOD));
        }

        public static Optional<Album> getAlbum09(){
            return Optional.of(new Album(9, "Back Fot That A$$", 2006, null, SampleArtist.getArtist05().orElseThrow(), Condition.VERY_GOOD_PLUS));
        }

        public static Optional<Album> getAlbum10(){
            return Optional.of(new Album(10, "Back Fot That A$$ pt. 2", 2006, null, SampleArtist.getArtist05().orElseThrow(), Condition.GOOD));
        }

        public static List<Album> getAllAlbums(){
            return Arrays.asList(getAlbum01().orElseThrow(), getAlbum02().orElseThrow(), getAlbum03().orElseThrow(),
                    getAlbum04().orElseThrow(), getAlbum05().orElseThrow(), getAlbum06().orElseThrow(),
                    getAlbum07().orElseThrow(), getAlbum08().orElseThrow(), getAlbum09().orElseThrow(),
                    getAlbum10().orElseThrow());
        }
    }
}
