package br.com.stew.fs;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.joda.time.DateTime;

import br.com.stew.fs.Processor.FlightInfo;
import br.com.stew.fs.processor.DecolarProcessor;

public class MainWindow
{
	private List<Processor>						processorPool;

	protected Shell								shell;
	private Table								tblReport;
	private Text								txtMaxLoops;
	private Text								txtDays;
	private Combo								cbFrom;
	private Combo								cbTo;
	private long								startTime;

	private org.eclipse.swt.widgets.DateTime	dtDeparture;

	private Label								lblPerSecond;

	private Label								lblSegs;

	private Label								lblItems;
	private Text								txtMinLoops;
	private Text								txtScale;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void open()
	{
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch())
			{
				display.sleep();
			}
		}
	}

	protected void createContents()
	{
		shell = new Shell();
		shell.setSize(1609, 605);
		shell.setText(Main.APP_NAME);

		shell.setLayout(new GridLayout(2, false));

		Composite composite = new Composite(shell, SWT.NONE);
		GridData gd_composite = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 1);
		gd_composite.widthHint = 225;
		composite.setLayoutData(gd_composite);
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		cbFrom = new Combo(composite, SWT.NONE);

		new Label(composite, SWT.NONE);

		cbTo = new Combo(composite, SWT.NONE);

		new Label(composite, SWT.NONE);

		for(Airport airport : Airport.values())
		{
			cbFrom.add(airport.toString());
		}
		for(Airport airport : Airport.values())
		{
			cbTo.add(airport.toString());
		}
		cbFrom.select(1);
		cbTo.select(1);

		dtDeparture = new org.eclipse.swt.widgets.DateTime(composite, SWT.BORDER);
		dtDeparture.setDate(dtDeparture.getYear(), dtDeparture.getMonth(), dtDeparture.getDay() + 1);

		Label lblMinRep = new Label(composite, SWT.NONE);
		lblMinRep.setText("min duracao");

		txtMinLoops = new Text(composite, SWT.BORDER);
		GridData gd_txtMinLoops = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMinLoops.widthHint = 91;
		txtMinLoops.setLayoutData(gd_txtMinLoops);
		txtMinLoops.setText("7");

		Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setText("max duracao");

		txtMaxLoops = new Text(composite, SWT.BORDER);
		GridData gd_txtMaxLoops = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtMaxLoops.widthHint = 105;
		txtMaxLoops.setLayoutData(gd_txtMaxLoops);
		txtMaxLoops.setText("7");

		Label lblDias = new Label(composite, SWT.NONE);
		lblDias.setText("dias");

		txtDays = new Text(composite, SWT.BORDER);
		GridData gd_txtDays = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_txtDays.widthHint = 104;
		txtDays.setLayoutData(gd_txtDays);
		txtDays.setText("30");

		Label lblParadas = new Label(composite, SWT.NONE);
		lblParadas.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblParadas.setText("paradas");

		txtScale = new Text(composite, SWT.BORDER);
		txtScale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtScale.setEnabled(false);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Label lblItems_1 = new Label(composite, SWT.NONE);
		lblItems_1.setText("items");

		lblItems = new Label(composite, SWT.NONE);
		lblItems.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblItems.setText("0");

		Label lblSegs2 = new Label(composite, SWT.NONE);
		lblSegs2.setText("segs");

		lblSegs = new Label(composite, SWT.NONE);
		lblSegs.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSegs.setText("0");

		Label lblIps = new Label(composite, SWT.NONE);
		lblIps.setText("per sec");

		lblPerSecond = new Label(composite, SWT.NONE);
		lblPerSecond.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblPerSecond.setText("0");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		Button btnNewButton = new Button(composite, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				start();
			}
		});
		btnNewButton.setText("New Button");
		new Label(composite, SWT.NONE);

		Composite composite_1 = new Composite(shell, SWT.NONE);
		GridData gd_composite_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_composite_1.widthHint = 765;
		composite_1.setLayoutData(gd_composite_1);
		composite_1.setLayout(new GridLayout(1, false));

		tblReport = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		tblReport.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tblReport.setHeaderVisible(true);
		tblReport.setLinesVisible(true);

		TableColumn tblclmnFrom = new TableColumn(tblReport, SWT.NONE);
		tblclmnFrom.setWidth(141);
		tblclmnFrom.setText("From");

		TableColumn tblclmnTo = new TableColumn(tblReport, SWT.NONE);
		tblclmnTo.setWidth(129);
		tblclmnTo.setText("To");

		TableColumn tblclmnDeparture = new TableColumn(tblReport, SWT.NONE);
		tblclmnDeparture.setText("Departure");
		tblclmnDeparture.setWidth(202);

		TableColumn tblclmnArrival = new TableColumn(tblReport, SWT.NONE);
		tblclmnArrival.setWidth(100);
		tblclmnArrival.setText("Arrival");

		TableColumn tblclmnPrice = new TableColumn(tblReport, SWT.NONE);
		tblclmnPrice.setWidth(100);
		tblclmnPrice.setText("Price");

		TableColumn tblclmnInfo = new TableColumn(tblReport, SWT.NONE);
		tblclmnInfo.setWidth(1000);
		tblclmnInfo.setText("Info");

		for(int i = 0; i < tblReport.getColumnCount(); i++)
		{
			final int finalI = i;
			final TableColumn column = tblReport.getColumn(i);

			column.addListener(SWT.Selection, new Listener()
			{
				@Override
				public void handleEvent(Event e)
				{
					TableItem[] items = column.getParent().getItems();
					Collator collator = Collator.getInstance(Locale.getDefault());
					for(int j = 1; j < items.length; j++)
					{
						String value1 = items[j].getText(finalI);
						for(int k = 0; k < j; k++)
						{
							String value2 = items[k].getText(finalI);
							if(collator.compare(value1, value2) < 0)
							{
								List<String> values = new ArrayList<String>();
								for(int l = 0; l < tblReport.getColumnCount(); l++)
								{
									values.add(items[j].getText(l));
								}
								items[j].dispose();
								TableItem item = new TableItem(column.getParent(), SWT.NONE, k);
								item.setText(values.toArray(new String[0]));
								items = column.getParent().getItems();
								break;
							}
						}
					}
				}
			});
		}

	}

	// ////////////////////////////////////////////////////////////////////////

	private void start()
	{
		tblReport.clearAll();

		startTime = 0;

		processorPool = Collections.synchronizedList(new ArrayList<Processor>());
		for(int i = 1; i <= 3; i++)
		{
			processorPool.add(new DecolarProcessor(null));
		}

		final DateTime startDate = new DateTime(dtDeparture.getYear(), dtDeparture.getMonth() + 1, dtDeparture.getDay(), 0, 0);

		final Airport from = Airport.values()[cbFrom.getSelectionIndex()];
		final Airport to = Airport.values()[cbTo.getSelectionIndex()];
		final Integer days = Integer.valueOf(txtDays.getText());
		final Integer minLoops = Integer.valueOf(txtMinLoops.getText());
		final Integer maxLoops = Integer.valueOf(txtMaxLoops.getText());
		final String scale = txtScale.getText();

		new Thread()
		{
			@Override
			public void run()
			{
				for(int i = 0; i < days; i++)
				{
					final int iFinal = i;
					for(int j = minLoops - 1; j < maxLoops; j++)
					{
						final int jFinal = j;

						Processor processor = null;
						while(processor == null)
						{
							synchronized (processorPool)
							{
								if(processorPool.size() > 0)
								{
									processor = processorPool.remove(0);
									break;
								}
							}
							try
							{
								Thread.sleep(1000);
							}
							catch(InterruptedException e)
							{
								e.printStackTrace();
							}
						}

						final Processor finalProcessor = processor;

						new Thread()
						{
							@Override
							public void run()
							{
								// System.gc();

								final DateTime departureDate = startDate.plusDays(iFinal);
								final DateTime arrivalDate = departureDate.plusDays(jFinal + 1);

								final FlightInfo flightInfo = finalProcessor.process(from, to, departureDate, arrivalDate, "var tmpParams = {scale:'" + scale + "'};");

								shell.getDisplay().asyncExec(new Runnable()
								{
									@Override
									public void run()
									{
										TableItem tableItem = new TableItem(tblReport, SWT.NONE);

										tableItem.setText(new String[] { from.getDesc(), to.getDesc(), departureDate.toString("dd/MM/yyyy") + " " + departureDate.dayOfWeek().getAsText(), arrivalDate.toString("dd/MM/yyyy"), String.valueOf(flightInfo.price), flightInfo.info });
										// tblReport.select(tblReport.getItemCount()
										// - 1);
										// tblReport.showSelection();
										tblReport.showItem(tableItem);

										if(startTime == 0)
										{
											startTime = new DateTime().getMillis() - 1000;
										}

										lblPerSecond.setText(String.valueOf(Float.valueOf(tblReport.getItemCount()) / (((new DateTime().getMillis()) - startTime) / 1000)));

										lblItems.setText(String.valueOf(tblReport.getItemCount()));
										lblSegs.setText(String.valueOf(((new DateTime().getMillis()) - startTime) / 1000));
									}
								});

								synchronized (processorPool)
								{
									processorPool.add(finalProcessor);
								}
							}
						}.start();
					}
				}

				for(int i = 1; i <= processorPool.size(); i++)
				{
					// processorPool.get(i).getBrowser().close();
					// processorPool.get(i).getBrowser().quit();
				}
			}
		}.start();

		System.out.println("END");
	}
}
