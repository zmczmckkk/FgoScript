package fgoScript.entity.guda;

public class TrainApGudaziForMainForMain extends ExpApGudaziForMainSpecial{
    @Override
    public void fightAndStop(boolean rebootFlag, int apNum) throws Exception {
        new EventGudazi().fightAndStop(rebootFlag, apNum);
    }
}
