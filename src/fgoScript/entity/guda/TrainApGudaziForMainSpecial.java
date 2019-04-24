package fgoScript.entity.guda;

public class TrainApGudaziForMainSpecial extends TrainApGudaziForLittleSpecial{
    @Override
    public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
        new EventGudazi().fightAndStop(rebootFlag, apNum);
    }
}
