package ch.abacus.application.view.dashboard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.util.SVGGenerator;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.data.DataProvider;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

import ch.abacus.application.common.repository.customer.CustomerDTO;
import ch.abacus.application.view.MenuDefinition;

@SpringView(name = "dashboard")
@MenuDefinition(translationKey = "Dashboard", icon = FontAwesome.TACHOMETER, order = 2)
public class DashboardViewBean extends VerticalLayout implements View {
	private static final long serialVersionUID = 3920179971480643945L;

	private Chart chart;

	@Autowired
	private DataProvider<CustomerDTO, String> customerDataProvider;

	private ComboBox<CustomerDTO> customerSelector;

	public DashboardViewBean() {
		setMargin(true);
		setSpacing(true);

		chart = new Chart(ChartType.COLUMN);
		DataSeries series = new DataSeries();
		series.add(new DataSeriesItem("a", 250));
		series.add(new DataSeriesItem("b", 500));
		series.add(new DataSeriesItem("c", 750));
		chart.getConfiguration().setSeries(series);

		chart.setWidth(400, Unit.PIXELS);

		customerSelector = new ComboBox<>("Select customer");

		addComponents(chart, createExportButton("chart.svg", createSVGStreamSource()), customerSelector);
	}

	@PostConstruct
	protected void initialize() {
		customerSelector.setDataProvider(customerDataProvider);
		customerSelector.setItemCaptionGenerator(c -> c.getFirstName() + " " + c.getLastName());
	}

	private StreamSource createSVGStreamSource() {
		return new StreamSource() {
			@Override
			public InputStream getStream() {
				String svg = SVGGenerator.getInstance().generate(chart.getConfiguration());
				if (svg != null) {
					return new ByteArrayInputStream(svg.getBytes());
				}
				return null;
			}
		};
	}

	private Button createExportButton(String filename, StreamSource stream) {
		Button export = new Button("Export", VaadinIcons.PRINT);
		FileDownloader downloader = new FileDownloader(new StreamResource(stream, filename));
		downloader.extend(export);
		return export;
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
