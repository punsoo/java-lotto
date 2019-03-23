package lotto;

import lotto.domain.LottoStore;
import lotto.domain.lotto.LottoBundle;
import lotto.domain.lotto.LottoNumber;
import lotto.domain.lotto.Ticket;
import lotto.domain.lotto.TicketMachine;
import lotto.domain.lotto.WinningLotto;
import lotto.utils.ManualLottoGenerator;
import lotto.utils.RandomLottoGenerator;
import lotto.view.InputView;
import lotto.view.LottoResult;
import lotto.view.OutputView;
import lotto.view.vo.MatchResult;

import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private static final String REGEX = ",";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int money = InputView.getMoney("구입금액을 입력해 주세요. ", scanner);
        int manualCount = InputView.getManualCount("\n수동으로 구매할 로또 수를 입력해 주세요.", scanner);
        List<String> manualLottoNumbers =
            InputView.getManualLottoNumbers("\n수동으로 구매할 번호를 입력해 주세요.", scanner, manualCount);
        List<Ticket> manualLottoTickets = TicketMachine.generateTickets(manualLottoNumbers, REGEX);
        LottoStore lottoStore =
            new LottoStore(new ManualLottoGenerator(), new RandomLottoGenerator(), money, manualLottoTickets);

        LottoBundle lottoBundle = lottoStore.buyManualLottos(manualLottoTickets);
        lottoBundle.addAll(lottoStore.buyRandomLottos());

        OutputView.printLottos(lottoBundle, manualCount);

        String winningLottoNumber =
            InputView.getWinningLottoNumbers("지난 주 당첨 번호를 입력해 주세요.", scanner);
        Ticket winningLottoTicket = TicketMachine.generateTicket(winningLottoNumber, REGEX);

        int bonusNumber = InputView.getBonusNumber("\n보너스 볼을 입력해 주세요.", scanner);

        WinningLotto winningLotto = new WinningLotto(winningLottoTicket, new LottoNumber(bonusNumber));

        LottoResult lottoResult = new LottoResult(winningLotto);
        MatchResult matchResult = lottoResult.generate(lottoBundle);
        OutputView.printResultStatistics(matchResult);
        OutputView.printRewardPercent(lottoResult.getRewardPercent(money));
    }
}