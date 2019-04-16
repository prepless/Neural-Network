package game.snake;


public class Score {
    private int points =0;
    private long startTime;
    private long elapsedTime;
    private long elapsedSeconds;
    private long secondsDisplay;
    private long elapsedMinutes;

    public void setPoints(){
        points++;
    }

    public int totalScore(){ return points; }

    public void setGameStartTime(){
        startTime = System.currentTimeMillis();
    }

    public String elapsedGameTime(){
        String elapsedGameTime = "M:"+Math.round(elapsedGameTimeMin())+", S:"+Math.round(elapsedGameTimeSec());
        return elapsedGameTime;
    }

    public double elapsedGameTimeSec(){
        elapsedTime = System.currentTimeMillis() - startTime;
        elapsedSeconds = elapsedTime / 1000;
        secondsDisplay = elapsedSeconds % 60;
        double elapsedGameTime = secondsDisplay;
        return elapsedGameTime;
    }

    public double elapsedGameTimeMin(){
        elapsedMinutes = elapsedSeconds / 60;
        double elapsedGameTime = elapsedMinutes;
        return elapsedGameTime;
    }

}
