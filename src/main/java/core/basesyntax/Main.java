package core.basesyntax;

import core.basesyntax.dao.OperationHandler;
import core.basesyntax.dao.OperationStrategy;
import core.basesyntax.impl.OperationStrategyImpl;
import core.basesyntax.impl.ReportGeneratorImpl;
import core.basesyntax.impl.ShopServiceImpl;
import core.basesyntax.operations.BalanceOperation;
import core.basesyntax.operations.PurchaseOperation;
import core.basesyntax.operations.ReturnOperation;
import core.basesyntax.operations.SupplyOperation;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        FileRider fileRider = new FileRider();
        final List<FruitTransaction> transactions =
                fileRider.readTransactions("src/main/resources/input.csv");

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = Map.of(
                FruitTransaction.Operation.BALANCE, new BalanceOperation(),
                FruitTransaction.Operation.SUPPLY, new SupplyOperation(),
                FruitTransaction.Operation.PURCHASE, new PurchaseOperation(),
                FruitTransaction.Operation.RETURN, new ReturnOperation()
        );

        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);

        Storage storage = new Storage();
        ShopService shopService = new ShopServiceImpl(operationStrategy, storage);
        shopService.process(transactions);

        ReportGenerator reportGenerator = new ReportGeneratorImpl();
        String resultingReport = reportGenerator.generateReport(storage);
        System.out.println("Generated report:\n" + resultingReport);

        core.basesyntax.FileWriterImpl fileWriter = new core.basesyntax.FileWriterImpl();
        fileWriter.write(resultingReport, "src/main/resources/finalReport.csv");
        System.out.println("Report written to finalReport.csv successfully.");
    }
}
