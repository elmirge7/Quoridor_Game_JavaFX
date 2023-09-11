package model;

import boardifier.model.GameStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class PawnUnitTest {

    @Test
    public void testCreatePawn() {
        GameStageModel stagemodel = Mockito.mock(GameStageModel.class);
        Pawn pawn = new Pawn(42, 5, stagemodel);
        Assertions.assertEquals(42, pawn.getNumber());
        Assertions.assertEquals(5, pawn.getColor());
    }
}
